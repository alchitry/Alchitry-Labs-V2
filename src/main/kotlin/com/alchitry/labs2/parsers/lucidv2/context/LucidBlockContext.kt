package com.alchitry.labs2.parsers.lucidv2.context

import com.alchitry.labs2.Log
import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.lucidv2.VerilogConverter
import com.alchitry.labs2.parsers.lucidv2.parsers.*
import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.types.Function
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.Value
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.tree.ParseTree

class LucidBlockContext(
    override val project: ProjectContext,
    override val sourceFile: SourceFile,
    val instance: TestOrModuleInstance,
    stage: ParseStage = ParseStage.ModuleInternals,
    override val evalContext: Evaluable? = null,
    val notationCollector: NotationCollector = project.notationManager.getCollector(sourceFile),
    expr: ExprParser? = null,
    bitSelection: BitSelectionParser? = null,
    types: TypesParser? = null,
    constant: ConstantParser? = null,
    enum: EnumParser? = null,
    struct: StructParser? = null,
    signal: SignalParser? = null,
    blockParser: BlockParser? = null,
    blockEvaluator: BlockEvaluator? = null,
    signalDriver: SignalDriverParser? = null,
    val localSignalResolver: SignalResolver? = null // Used in tests to simulate a full parse.
) : LucidExprContext, ErrorListener by notationCollector {
    var stage: ParseStage = stage
        private set

    private val localSignalStack = mutableListOf<MutableMap<String, Signal>>(mutableMapOf())
    val localSignals: MutableMap<String, Signal> get() = localSignalStack.last()

    private var takeSnapshot: suspend () -> Unit = {}
    fun setSnapshotCallback(onSnapshot: suspend () -> Unit) {
        takeSnapshot = onSnapshot
    }

    suspend fun tick(shouldSnapshot: Boolean) {
        if (stage != ParseStage.Evaluation)
            return
        blockEvaluator.processWriteQueue()
        project.processQueue()
        if (shouldSnapshot)
            takeSnapshot()
    }

    fun abortTest() {
        if (stage != ParseStage.Evaluation)
            return
        throw TestAbortedException()
    }

    fun print(text: String) {
        if (stage != ParseStage.Evaluation)
            return
        Log.println(text)
    }

    fun printError(text: String) {
        if (stage != ParseStage.Evaluation)
            return
        Log.printlnError(text)
    }

    // These will be run multiple times during evaluation, so they need their context updated
    val expr = expr?.withContext(this) ?: ExprParser(this)
    val bitSelection = bitSelection?.withContext(this) ?: BitSelectionParser(this)
    val signal = signal?.withContext(this) ?: SignalParser(this)
    val blockEvaluator = blockEvaluator?.withContext(this) ?: BlockEvaluator(this)

    // These won't be re-run during evaluation, so they don't need their context updated
    val types = types ?: TypesParser(this)
    val struct = struct ?: StructParser(this)
    val enum = enum ?: EnumParser(this)
    val constant = constant ?: ConstantParser(this)
    val blockParser = blockParser ?: BlockParser(this)
    val signalDriver = signalDriver ?: SignalDriverParser(this)
    val verilogConverter = VerilogConverter(this)

    private val listenerMap = ParseStage.entries.associateWith {
        when (it) {
            ParseStage.ModuleInternals -> listOf(
                this.expr,
                this.bitSelection,
                this.struct,
                this.enum,
                this.constant,
                this.types,
                this.blockParser,
                this.signal
            )

            ParseStage.Drivers -> listOf(
                this.expr,
                this.bitSelection,
                this.signal,
                this.signalDriver
            )

            ParseStage.Evaluation -> listOf(
                this.expr,
                this.bitSelection,
                this.signal,
                this.blockEvaluator
            )

            ParseStage.ErrorCheck -> listOf(
                this.expr,
                this.bitSelection,
                this.struct,
                this.enum,
                this.constant,
                this.signal,
                this.types,
                this.blockParser
            )

            ParseStage.Convert -> listOf(
                this.signal,
                this.verilogConverter
            )
        }
    }

    fun withEvalContext(evalContext: Evaluable, name: String) = LucidBlockContext(
        project,
        sourceFile,
        instance,
        ParseStage.Evaluation,
        evalContext,
        notationCollector.createChild(name),
        expr,
        bitSelection,
        types,
        constant,
        enum,
        struct,
        signal,
        blockParser,
        blockEvaluator,
        signalDriver,
        localSignalResolver
    )

    private suspend fun reset() {
        types.sigs.values.forEach { it.write(it.initialValue) } // reset sigs to 'x'
        types.dffs.values.forEach { it.reset() }
        types.getInstances().forEach { it.context.reset() }
    }

    private suspend fun queueEval() {
        blockParser.queueEval()
        types.getInstances().forEach { it.context.queueEval() }
    }

    /**
     * Queues always blocks for an initial evaluation.
     */
    suspend fun initialize() {
        reset()
        project.initialize() // propagate initial signal values
        queueEval()
        project.initialize()
    }

    suspend fun initialWalk(t: ParseTree): Boolean {
        require(stage == ParseStage.ModuleInternals) { "initialWalk called on an already initialized context!" }
        walk(t)
        if (notationCollector.hasErrors)
            return false
        stage = ParseStage.Drivers
        walk(t)
        stage = ParseStage.Evaluation
        return notationCollector.hasNoErrors
    }

    suspend fun walk(t: ParseTree) = ParseTreeMultiWalker.walk(listenerMap[stage]!!, t, stage.filter)

    suspend fun checkParameters(errorListener: ErrorListener = notationCollector): Boolean {
        val instance = (instance as? ModuleInstance)
            ?: error("checkParameters() can only be called on contexts with a ModuleInstance!")
        stage = ParseStage.Evaluation
        instance.module.parameters.values.forEach { param ->
            param.constraint?.let {
                walk(it)
                if (resolve(it)?.isTrue()?.bit != Bit.B1) {
                    errorListener.reportError(
                        it,
                        "Parameter constraint failed for ${param.name} (${param.constraint.text})"
                    )
                    stage = ParseStage.ModuleInternals
                    return false
                }
            }
        }
        stage = ParseStage.ModuleInternals
        return true
    }

    suspend fun convertToVerilog(): String? {
        val instance = (instance as? ModuleInstance)
            ?: error("convertToVerilog() can only be called on contexts with a ModuleInstance!")
        stage = ParseStage.Convert
        walk(instance.module.context)
        return verilogConverter.verilog[instance.module.context]
    }

    override fun resolve(exprCtx: ExprContext) = expr.resolve(exprCtx)

    override fun resolve(signalCtx: SignalContext) = signal.resolve(signalCtx)
    override fun resolve(signalWidthContext: SignalWidthContext) = signal.resolve(signalWidthContext)
    override fun resolve(structTypeContext: StructTypeContext) = struct.resolve(structTypeContext)
    override fun resolve(structDecContext: StructDecContext) = struct.resolve(structDecContext)
    override fun resolve(enumDecContext: EnumDecContext) = enum.resolve(enumDecContext)
    override fun resolve(constDecContext: ConstDecContext) = constant.resolve(constDecContext)
    override fun resolve(bitSelectionContext: BitSelectionContext) = bitSelection.resolve(bitSelectionContext)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    override fun resolveSignal(name: String): SignalOrParent? {
        localSignalResolver?.resolve(name)?.let { return it }
        localSignals[name]?.let { return it }
        types.resolve(name)?.let { return it }
        constant.resolve(name)?.let { return it }
        enum.resolve(name)?.let { return it }

        (instance as? ModuleInstance)?.getInternalSignal(name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String) = project.resolveGlobal(name)

    override fun resolveFunction(name: String): Function? {
        Function.builtIn.firstOrNull { it.label == name }?.let { return it }
        return blockParser.resolveFunction(name)
    }

    suspend fun runFunction(function: Function.Custom, args: List<Value>) {
        if (stage != ParseStage.Evaluation)
            return

        localSignalStack.add(mutableMapOf())

        function.args.forEachIndexed { idx, (name, width, signed) ->
            localSignals[name] = args[idx].resizeToMatch(width).withSign(signed).asMutable().asSignal(name, null)
        }

        walk(function.functionBlock.functionBody()?.block() ?: error("Missing function body block!"))

        localSignalStack.removeLast()
    }
}
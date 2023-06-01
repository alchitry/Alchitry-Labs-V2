package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.types.Function
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.TestAbortedException
import com.alchitry.labs.parsers.lucidv2.types.TestOrModuleInstance
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.runBlocking
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeListener

class LucidBlockContext(
    override val project: ProjectContext,
    val instance: TestOrModuleInstance,
    var stage: ParseStage = ParseStage.ModuleInternals,
    override val evalContext: Evaluable? = null,
    val errorCollector: ErrorCollector = ErrorCollector(),
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
) : LucidExprContext, ErrorListener by errorCollector {

    private val localSignalStack = mutableListOf<MutableMap<String, Signal>>(mutableMapOf())
    val localSignals: MutableMap<String, Signal> get() = localSignalStack.last()

    fun tick() {
        if (stage != ParseStage.Evaluation)
            return
        runBlocking {
            blockEvaluator.processWriteQueue()
            project.processQueue()
        }
    }

    fun abortTest() {
        if (stage != ParseStage.Evaluation)
            return
        throw TestAbortedException()
    }

    fun print(text: String) {
        if (stage != ParseStage.Evaluation)
            return
        println(text)
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

    private fun getListeners() = when (stage) {
        ParseStage.ModuleInternals -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.struct,
            this.enum,
            this.constant,
            this.types,
            this.blockParser,
            this.signal
        )

        ParseStage.Drivers -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.signalDriver
        )

        ParseStage.Evaluation -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.blockEvaluator
        )

        ParseStage.ErrorCheck -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.struct,
            this.enum,
            this.constant,
            this.signal,
            this.types,
            this.blockParser
        )
    }

    private fun getFilter(): WalkerFilter = when (stage) {
        ParseStage.ModuleInternals -> WalkerFilter.SkipGlobals
        ParseStage.Drivers -> WalkerFilter.SkipGlobals
        ParseStage.Evaluation -> WalkerFilter.join(WalkerFilter.SkipControlBlocks, WalkerFilter.SkipGlobals)
        ParseStage.ErrorCheck -> WalkerFilter.SkipGlobals
    }

    fun withEvalContext(evalContext: Evaluable) = LucidBlockContext(
        project,
        instance,
        ParseStage.Evaluation,
        evalContext,
        ErrorCollector(), // give each context its own error collector
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

    /**
     * Queues always blocks for an initial evaluation.
     */
    suspend fun initialize() {
        blockParser.queueEval()
        types.getInstances().forEach { it.context.initialize() }
        project.initialize()
    }

    fun initialWalk(t: ParseTree): List<String>? {
        stage = ParseStage.ModuleInternals
        walk(t)
        if (errorCollector.errors.isNotEmpty())
            return errorCollector.errors.toList()
        stage = ParseStage.Drivers
        walk(t)
        if (errorCollector.errors.isNotEmpty())
            return errorCollector.errors.toList()
        return null
    }

    fun walk(t: ParseTree) = ParseTreeMultiWalker.walk(getListeners(), t, getFilter())

    fun checkParameters(): List<String>? {
        val instance = (instance as? ModuleInstance)
            ?: error("checkParameters() can only be called on contexts with a ModuleInstance!")
        stage = ParseStage.Evaluation
        instance.module.parameters.values.forEach { param ->
            param.constraint?.let {
                walk(it)
                if (resolve(it)?.isTrue()?.bit != Bit.B1) {
                    errorCollector.reportError(it, "Parameter constraint failed for ${param.name}.")
                    return errorCollector.errors.toList()
                }
            }
        }
        return null
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
        Function.builtIn().firstOrNull { it.label == name }?.let { return it }
        return blockParser.resolveFunction(name)
    }

    fun runFunction(function: Function.Custom, args: List<Value>) {
        if (stage != ParseStage.Evaluation)
            return

        localSignalStack.add(mutableMapOf())

        function.args.forEachIndexed { idx, (name, width) ->
            localSignals[name] = args[idx].resizeToMatch(width).asSignal(name, null)
        }

        walk(function.functionBlock.block())

        localSignalStack.removeLast()
    }
}
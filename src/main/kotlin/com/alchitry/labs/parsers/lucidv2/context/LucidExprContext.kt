package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.Evaluable
import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.NotationCollector
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.types.*
import com.alchitry.labs.parsers.lucidv2.types.Function
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value
import org.antlr.v4.kotlinruntime.tree.ParseTree

interface LucidExprContext : ErrorListener {
    fun resolve(exprCtx: ExprContext): Value?
    fun resolve(bitSelectionContext: BitSelectionContext): List<BitSelection>
    fun resolve(signalCtx: SignalContext): SignalOrSubSignal?
    fun resolve(signalWidthContext: SignalWidthContext): SignalWidth?
    fun resolve(structTypeContext: StructTypeContext): StructType?
    fun resolve(structDecContext: StructDecContext): StructType?
    fun resolve(enumDecContext: EnumDecContext): EnumType?
    fun resolve(constDecContext: ConstDecContext): Constant?
    fun resolveSignal(name: String): SignalOrParent?
    fun resolveStruct(name: String): StructType?
    fun resolveGlobal(name: String): GlobalNamespace?
    fun resolveFunction(name: String): Function? = Function.builtIn.firstOrNull { it.label == name }
    val evalContext: Evaluable?
    val project: ProjectContext
}

class LucidExprEval(
    override val project: ProjectContext,
    val notationCollector: NotationCollector,
    val signalProvider: (name: String) -> SignalOrParent?
) : LucidExprContext, ErrorListener by notationCollector {
    override val evalContext: Evaluable? = null

    private val expr = ExprParser(this)
    private val bitSelection = BitSelectionParser(this)
    private val struct = StructParser(this)
    private val signal = SignalParser(this)
    private val module = ModuleParser(this)

    private val listeners = listOf(
        this.expr,
        this.bitSelection,
        this.struct,
        this.signal,
        this.module
    )

    suspend fun walk(t: ParseTree) {
        ParseTreeMultiWalker.walk(
            listeners,
            t,
            WalkerFilter.None
        )
    }

    override fun resolve(exprCtx: LucidParser.ExprContext) = expr.resolve(exprCtx)

    override fun resolve(signalCtx: LucidParser.SignalContext) = signal.resolve(signalCtx)
    override fun resolve(signalWidthContext: LucidParser.SignalWidthContext) = signal.resolve(signalWidthContext)
    override fun resolve(structTypeContext: LucidParser.StructTypeContext) = struct.resolve(structTypeContext)
    override fun resolve(structDecContext: LucidParser.StructDecContext) = struct.resolve(structDecContext)
    override fun resolve(enumDecContext: LucidParser.EnumDecContext): EnumType? = null
    override fun resolve(constDecContext: LucidParser.ConstDecContext): Constant? = null

    override fun resolve(bitSelectionContext: LucidParser.BitSelectionContext) =
        bitSelection.resolve(bitSelectionContext)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    override fun resolveSignal(name: String): SignalOrParent? {
        signalProvider(name)?.let { return it }
        module.resolve(name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String) = project.resolveGlobal(name)
}

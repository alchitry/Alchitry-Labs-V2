package com.alchitry.labs2.parsers.hdl

import com.alchitry.labs2.asSingleLine
import com.alchitry.labs2.parsers.hdl.lucid.parsers.checkSimpleOrCompatible
import com.alchitry.labs2.parsers.hdl.lucid.parsers.checkSimpleWidth
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.*
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.parsers.notations.ErrorStrings
import com.alchitry.labs2.parsers.notations.WarningStrings
import com.alchitry.labs2.parsers.parents
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.RuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree
import java.math.BigInteger

interface ExprEvaluatorContext<T : ParserRuleContext> : ErrorListener {
    fun resolve(exprCtx: T): Expr?
    val mode: ExprEvalMode
}

enum class ExprEvalMode {
    Default,
    Testing,
    Building
}

data class ExprEvaluator<T : ParserRuleContext>(
    private val context: ExprEvaluatorContext<T>,
    private val exprs: MutableMap<ParseTree, Expr> = mutableMapOf(),
    private val assignWidths: MutableMap<ParseTree, SignalWidth> = mutableMapOf(),
    private val widthFence: MutableMap<ParseTree, Boolean> = mutableMapOf(),
    private val dependencies: MutableMap<ParseTree, Set<Signal>> = mutableMapOf(),
    private val deadBlocks: MutableMap<RuleContext, Boolean> = mutableMapOf(),
) {
    fun withContext(context: ExprEvaluatorContext<T>) = copy(context = context)

    fun resolve(ctx: ParseTree): Expr? = exprs[ctx]
    fun resolveDependencies(ctx: ParseTree): Set<Signal>? = dependencies[ctx]

    private fun getExprType(exprCtx: List<ParseTree>): ExprType? =
        getExprType(exprCtx.map { exprs[it]?.type ?: return null })

    fun getExprType(dependants: Collection<ExprType>): ExprType {
        return ExprType.entries[dependants.minOfOrNull { it.ordinal } ?: return ExprType.Constant]
    }

    fun setAssignWidth(ctx: ParseTree, width: SignalWidth) {
        assignWidths[ctx] = width
    }

    private val finalWidthCache: MutableMap<ParseTree, SignalWidth> = mutableMapOf()

    fun getFinalWidth(ctx: RuleContext): SignalWidth? {
        finalWidthCache[ctx]?.let { return it }

        if (widthFence[ctx] == true)
            return exprs[ctx]?.value?.width

        val parents = ctx.parents
        val fenceIdx = parents.indexOfFirst { widthFence[it] == true }
        val trimmed = if (fenceIdx >= 0) parents.subList(0, fenceIdx) else parents

        val assignWidth = trimmed.asReversed().firstNotNullOfOrNull { assignWidths[it] }
        val exprWidth = trimmed.asReversed().firstNotNullOfOrNull { exprs[it] }?.value?.width

        if (exprWidth != null) {
            if (assignWidth != null) {
                if (assignWidth is SimpleWidth && exprWidth is SimpleWidth) {
                    val w = assignWidth.mergeWith(exprWidth)
                    finalWidthCache[ctx] = w
                    return w
                }
                finalWidthCache[ctx] = assignWidth
                return assignWidth
            }
            finalWidthCache[ctx] = exprWidth
            return exprWidth
        }

        exprs[ctx]?.value?.width?.let {
            finalWidthCache[ctx] = it
            return it
        }
        return null
    }

    fun isDeadBlock(ctx: RuleContext): Boolean = deadBlocks[ctx] == true

    fun inDeadBlock(ctx: RuleContext): Boolean {
        var current: RuleContext? = ctx
        while (current != null) {
            if (deadBlocks[current] == true)
                return true
            current = current.parent
        }
        return false
    }

    fun setDeadBlock(ctx: RuleContext, isDead: Boolean) {
        deadBlocks[ctx] = isDead
    }

    /**
     * Returns true if the value at this node doesn't need to be recalculated
     */
    fun canSkip(ctx: ParseTree): Boolean {
        val expr = exprs[ctx] ?: return false
        if (expr.type == ExprType.Constant && expr.value !is UndefinedValue) { // TODO: remove undefined check?
            ctx.skip = true
            return true
        }
        return false
    }

    fun setDependencies(ctx: ParseTree, dependants: Set<Signal>) {
        dependencies[ctx] = dependants
    }

    fun copyDependencies(ctx: ParseTree, children: List<ParseTree>?) {
        if (children == null) return
        if (dependencies[ctx] != null) return
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    fun copyDependencies(ctx: ParseTree, child: ParseTree?) {
        if (dependencies[ctx] != null) return
        dependencies[child ?: return]?.let { dependencies[ctx] = it }
    }

    fun setExpr(ctx: ParseTree, expr: Expr) {
        exprs[ctx] = expr
    }

    fun copyExpr(ctx: ParseTree, child: ParseTree?) {
        exprs[ctx] = exprs[child ?: return] ?: return
    }

    fun setWidthFence(ctx: ParseTree) {
        widthFence[ctx] = true
    }

    fun passThrough(ctx: ParseTree, child: ParseTree?) {
        if (canSkip(ctx)) return
        copyExpr(ctx, child)
        copyDependencies(ctx, child)
    }

    fun concatenate(ctx: ParserRuleContext, children: List<T>) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        if (children.isEmpty()) return

        copyDependencies(ctx, children)

        val type = getExprType(children) ?: return

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        children.forEach {
            val v = exprs[it]?.value ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val baseSigWidth = operands.map { it.first.width }.firstMostDefined()
        var error = false

        if (!baseSigWidth.isArrayOrSimple()) {
            context.reportError(operands[0].second, "Concatenation can't be performed on structs")
            return
        }

        var dimSize = 0
        var definedWidth = true

        operands.forEach {
            val sigWidth = it.first.width
            if (!sigWidth.canConcat(baseSigWidth)) {
                context.reportError(
                    it.second,
                    "Each element in an array concatenation must have the same dimensions"
                )
                error = true
            }
            when (sigWidth) {
                is UndefinedWidth -> definedWidth = false
                is DefinedArrayWidth -> dimSize += sigWidth.size
                is DefinedSimpleWidth -> dimSize += sigWidth.size
                is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
            }
        }

        if (error) return

        if (operands.any { it.first is UndefinedValue }) {
            val width = if (definedWidth) {
                when (baseSigWidth) {
                    is SimpleWidth -> BitListWidth(dimSize)
                    is ArrayWidth -> DefinedArrayWidth(dimSize, baseSigWidth.next)
                    is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
                }
            } else {
                when (baseSigWidth) {
                    is SimpleWidth -> UndefinedSimpleWidth()
                    is ArrayWidth -> UndefinedArrayWidth(baseSigWidth.next)
                    is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
                }
            }
            exprs[ctx] = UndefinedValue(width).asExpr(type)
            return
        }

        exprs[ctx] = when (baseSigWidth) {
            is SimpleWidth -> {
                val bits = mutableListOf<Bit>()
                operands.asReversed().forEach { bits.addAll((it.first as SimpleValue).bits) }
                BitListValue(bits, signed = false).asExpr(type)
            }

            is ArrayWidth -> {
                val valueList = mutableListOf<Value>()
                operands.asReversed().forEach { valueList.addAll((it.first as ArrayValue).elements) }
                ArrayValue(valueList).asExpr(type)
            }

            is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
        }
    }

    fun duplicate(ctx: ParserRuleContext, children: List<ParserRuleContext>) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        if (children.size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }


        val dupCountExpr = exprs[children[0]] ?: return
        val dupValueExpr = exprs[children[1]] ?: return

        if (!dupCountExpr.type.known) {
            context.reportError(children[0], "The expression \"${children[0].text.asSingleLine()}\" must be constant.")
            return
        }

        val valWidth = dupValueExpr.value.width

        if (!valWidth.isArrayOrSimple()) {
            context.reportError(children[0], "Duplication can't be performed on structs.")
            return
        }

        if (dupCountExpr.value.width !is SimpleWidth) {
            context.reportError(children[0], "The array duplication index must be one dimensional.")
            return
        }

        // if the duplication value is undefined, we have no idea what the width will be
        if (dupCountExpr.value is UndefinedValue) {
            exprs[ctx] = UndefinedValue().asExpr(dupValueExpr.type)
            return
        }

        assert(dupCountExpr.value is SimpleValue) { "Duplication count is flat array but not a SimpleValue!" }
        dupCountExpr.value as SimpleValue

        if (!dupCountExpr.value.isNumber()) {
            context.reportError(children[0], "The array duplication index must be a number (no x or z values).")
            return
        }

        val dupTimes = try {
            dupCountExpr.value.toBigInt()!!.intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(children[0], "Duplication count is too big to fit into an integer!")
            return
        }

        if (dupValueExpr.value is UndefinedValue) {
            exprs[ctx] = UndefinedValue(
                width = when (valWidth) {
                    is DefinedArrayWidth -> DefinedArrayWidth(valWidth.size * dupTimes, valWidth.next)
                    is DefinedSimpleWidth -> BitListWidth(valWidth.size * dupTimes)
                    is UndefinedSimpleWidth -> valWidth
                    is UndefinedArrayWidth -> valWidth
                    is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
                }
            ).asExpr(dupValueExpr.type)
            return
        }

        if (dupTimes <= 0) {
            context.reportError(children[0], "Duplication count must be greater than 0.")
            return
        }

        if (dupValueExpr.value is ArrayValue) {
            val elements = mutableListOf<Value>()
            repeat(dupTimes) {
                elements.addAll(dupValueExpr.value.elements)
            }
            exprs[ctx] = ArrayValue(elements).asExpr(dupValueExpr.type)
        } else if (dupValueExpr.value is SimpleValue) {
            val bits = mutableListOf<Bit>()
            repeat(dupTimes) {
                bits.addAll(dupValueExpr.value.bits)
            }
            exprs[ctx] = BitListValue(bits, dupValueExpr.value.signed).asExpr(dupValueExpr.type)
        }
    }

    fun buildArray(ctx: T, children: List<T>) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        if (children.isEmpty())
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val operands = mutableListOf<Pair<Expr, ParserRuleContext>>()
        children.forEach {
            val v = exprs[it] ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val filteredValues = operands.map {
            val v = (it.first.value as? BitValue)?.asBitListValue()?.asExpr(it.first.type) ?: it.first
            v
        }

        val arrayDim = filteredValues.map { it.value.width }.firstMostDefined()

        val errors = operands.map {
            // check if the widths match, or at least one is an UndefinedSimpleWidth, and they're both simple
            if (!it.first.value.width.isCompatibleWith(arrayDim)) {
                context.reportError(it.second, "Each element in an array builder must have the same dimensions")
                true
            } else {
                false
            }
        }

        exprs[ctx] = ArrayValue(filteredValues.mapIndexed { index, expr ->
            if (errors[index] || expr.value.width is UndefinedWidth)
                UndefinedValue(arrayDim)
            else
                expr.value
        }.asReversed()).asExpr(getExprType(filteredValues.map { it.type }))
    }

    fun negate(ctx: T, child: ParserRuleContext?) {
        if (canSkip(ctx)) return

        copyDependencies(ctx, child)

        val expr = exprs[child ?: return] ?: return

        if (!expr.value.width.isSimple()) {
            context.reportError(ctx, "Only single dimensional arrays can be negated")
            return
        }

        if (expr.value is UndefinedValue) {
            exprs[ctx] = UndefinedValue(expr.value.width).asExpr(expr.type)
            return
        }

        assert(expr.value is SimpleValue) { "Expression assumed to be SimpleValue" }
        expr.value as SimpleValue

        if (!expr.value.isNumber()) {
            exprs[ctx] = BitListValue(size = expr.value.size + 1, signed = false) { Bit.Bx }.asExpr(expr.type)
            return
        }

        exprs[ctx] = BitListValue(expr.value.toBigInt()!!.negate(), true, expr.value.size + 1).asExpr(expr.type)
    }

    /**
     * Accepts ! or ~ operators.
     */
    fun invert(ctx: T, child: ParserRuleContext?, operator: String) {
        if (canSkip(ctx)) return

        val expr = exprs[child ?: return] ?: return

        copyDependencies(ctx, child)

        exprs[ctx] = if (operator == "!") {
            if (expr.value is UndefinedValue)
                UndefinedValue(BitWidth).asExpr(expr.type)
            else
                expr.value.isTrue().not().asExpr(expr.type)
        } else { // ~ operator
            expr.value.invert().asExpr(expr.type)
        }
    }

    fun addOrSubtract(ctx: T, children: List<T>, operator: String) {
        if (canSkip(ctx)) return

        if (children.size != 2)
            return

        copyDependencies(ctx, children)

        val type = getExprType(children) ?: return

        val op1 = exprs[children[0]]?.value ?: return
        val op2 = exprs[children[1]]?.value ?: return

        if (!context.checkSimpleWidth(children) {
                context.reportError(
                    it,
                    "Only single dimensional arrays can be ${if (operator == "+") "added" else "subtracted"}"
                )
            }) return

        val op1Width = op1.width
        val op2Width = op2.width

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (op1Width is DefinedSimpleWidth && op2Width is DefinedSimpleWidth)
                exprs[ctx] =
                    UndefinedValue(
                        BitListWidth(op1Width.size.coerceAtLeast(op2Width.size) + 1)
                    ).asExpr(type)
            else
                exprs[ctx] = UndefinedValue().asExpr(type)
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a SimpleValue. This shouldn't be possible.")

        val width = op1.bits.size.coerceAtLeast(op2.bits.size) + 1

        val signed = op1.signed && op2.signed

        exprs[ctx] = when {
            !op1.isNumber() || !op2.isNumber() -> BitListValue(
                size = width,
                signed = signed
            ) { Bit.Bx }.asExpr(type)

            operator == "+" -> BitListValue(
                bigInt = op1.toBigInt()!!.add(op2.toBigInt()),
                signed = signed,
                width = width
            ).asExpr(type)

            else -> BitListValue(
                bigInt = op1.toBigInt()!!.subtract(op2.toBigInt()),
                signed = signed,
                width = width
            ).asExpr(type)
        }
    }

    fun multiplyOrDivide(ctx: T, children: List<T>, operator: String) {
        if (canSkip(ctx)) return

        if (children.size != 2) return

        copyDependencies(ctx, children)

        val type = getExprType(children) ?: return

        val op1 = exprs[children[0]]?.value ?: return
        val op2Expr = exprs[children[1]] ?: return
        val op2 = op2Expr.value

        val multOp = operator == "*"

        if (!context.checkSimpleWidth(children) {
                context.reportError(
                    it,
                    "Only single dimensional arrays can be ${if (multOp) "multiplied" else "divided"}"
                )
            }) return

        val op1Width = op1.width
        val op2Width = op2.width

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            exprs[ctx] = when {
                multOp && op1Width is DefinedSimpleWidth && op2Width is DefinedSimpleWidth -> {
                    val finalWidth = getFinalWidth(ctx)
                    val mulWidth = op1Width.size + op2Width.size
                    val width = finalWidth?.let { it.bitCount?.coerceAtLeast(mulWidth) } ?: mulWidth
                    UndefinedValue(BitListWidth(width)).asExpr(type)
                }

                !multOp && op1Width is DefinedSimpleWidth -> UndefinedValue(BitListWidth(op1Width.size)).asExpr(type)
                else -> UndefinedValue().asExpr(type)
            }
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a simple array. This shouldn't be possible.")

        val signed = op1.signed && op2.signed
        op1Width as DefinedSimpleWidth
        op2Width as DefinedSimpleWidth

        exprs[ctx] = (if (multOp) {
            val finalWidth = getFinalWidth(ctx)
            val mulWidth = op1Width.size + op2Width.size
            val width = finalWidth?.let { it.bitCount?.coerceAtLeast(mulWidth) } ?: mulWidth
            if (!op1.isNumber() || !op2.isNumber())
                BitListValue(size = width, signed = signed) { Bit.Bx }
            else
                BitListValue(
                    bigInt = op1.toBigInt(signed)!!.multiply(op2.toBigInt(signed)),
                    signed = signed,
                    width = width
                )
        } else {
            val op2BigInt = op2.toBigInt(signed)!!

            if (!type.known && (!op2Expr.type.known || !op2.isPowerOf2())) {
                context.reportWarning(children[1], WarningStrings.DIVIDE_NOT_POW_2)
            }

            val width = op1.size
            if (!op1.isNumber() || !op2.isNumber() || op2BigInt == BigInteger.ZERO)
                BitListValue(size = width, signed = signed) { Bit.Bx }
            else
                BitListValue(
                    bigInt = op1.toBigInt(signed)!!.divide(op2BigInt),
                    signed = signed,
                    width = width
                )
        }).asExpr(type)
    }

    fun shift(ctx: T, children: List<T>, operator: String) {
        if (canSkip(ctx)) return

        if (children.size != 2) return

        copyDependencies(ctx, children)

        val type = getExprType(children) ?: return

        val value = exprs[children[0]]?.value ?: return
        val shift = exprs[children[1]]?.value ?: return

        if (!context.checkSimpleWidth(children) {
                context.reportError(it, ErrorStrings.SHIFT_MULTI_DIM)
            }) return

        if (shift is UndefinedValue) {
            exprs[ctx] = UndefinedValue().asExpr(type)
            return
        }

        check(shift is SimpleValue) { "Shift value has simple width but is not SimpleValue or UndefinedValue" }

        if (value is UndefinedValue) {
            val vWidth = value.width
            exprs[ctx] = if (vWidth is DefinedSimpleWidth && shift.isNumber()) {
                val w = if (operator == "<<" || operator == "<<<") vWidth.size + shift.toBigInt()!!
                    .toInt() else vWidth.size
                UndefinedValue(BitListWidth(w)).asExpr(type)
            } else {
                UndefinedValue().asExpr(type)
            }
            return
        }

        check(value is SimpleValue) { "Value is flat array but not SimpleValue or UndefinedValue" }

        val isSigned = value.signed && (operator == ">>>" || operator == "<<<")

        if (!shift.isNumber()) {
            exprs[ctx] = BitListValue(value.size, isSigned) { Bit.Bx }.asExpr(type)
            return
        }

        val shiftAmount = try {
            shift.toBigInt()!!.intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(children[1], "Shift amount didn't fit into an integer!")
            return
        }

        exprs[ctx] = when (operator) {
            ">>" -> value ushr shiftAmount
            ">>>" -> value shr shiftAmount
            "<<" -> value ushl shiftAmount
            "<<<" -> value shl shiftAmount
            else -> {
                context.reportError(ctx, "Unknown operator $operator")
                return
            }
        }.asExpr(type)
    }

    fun bitwise(ctx: T, children: List<T>, operator: String) {
        if (canSkip(ctx)) return
        if (children.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(children) ?: return

        val op1 = exprs[children[0]]?.value ?: return
        val op2 = exprs[children[1]]?.value ?: return

        if (!context.checkSimpleOrCompatible(children[0], children[1]) {
                context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operator))
            }) return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            exprs[ctx] = UndefinedValue(op1.width.mergeWith(op2.width)).asExpr(type)
            return
        }

        exprs[ctx] = when (operator) {
            "&" -> op1 and op2
            "|" -> op1 or op2
            "^" -> op1 xor op2
            else -> {
                context.reportError(ctx, "Unknown operator $operator")
                return
            }
        }.asExpr(type)
    }

    fun reduction(ctx: ParserRuleContext, child: ParserRuleContext?, operator: String) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        copyDependencies(ctx, child)

        val expr = exprs[child ?: return] ?: return
        val value = expr.value

        if (value is UndefinedValue) {
            exprs[ctx] = UndefinedValue(BitWidth).asExpr(expr.type)
            return
        }

        exprs[ctx] = when (operator.replace("~", "")) {
            "&" -> value.andReduce()
            "|" -> value.orReduce()
            "^" -> value.xorReduce()
            "~&" -> value.andReduce().invert()
            "~|" -> value.orReduce().invert()
            "~^" -> value.xorReduce().invert()
            "^~" -> value.invert().xorReduce()
            else -> {
                context.reportError(
                    ctx.getChild(ParserRuleContext::class, 0) ?: return,
                    "Unknown operator ${ctx.getChild(0)?.text}"
                )
                return
            }
        }.asExpr(expr.type)
    }

    fun comparison(ctx: T, children: List<T>, operator: String) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        if (children.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(children) ?: return

        val op1 = exprs[children[0]]?.value ?: return
        val op2 = exprs[children[1]]?.value ?: return

        when (operator) {
            "<", ">", "<=", ">=" -> {
                if (!context.checkSimpleWidth(children) {
                        context.reportError(it, "The operator \"$operator\" can only be used on simple values.")
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    exprs[ctx] = UndefinedValue(BitWidth).asExpr(type)
                    return
                }

                op1 as SimpleValue
                op2 as SimpleValue

                exprs[ctx] = when (operator) {
                    "<" -> op1 isLessThan op2
                    ">" -> op1 isGreaterThan op2
                    "<=" -> op1 isLessOrEqualTo op2
                    ">=" -> op1 isGreaterOrEqualTo op2
                    else -> error("Unknown operand!")
                }.asExpr(type)
            }

            "==", "!=" -> {
                if (!context.checkSimpleOrCompatible(children) {
                        context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operator))
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    exprs[ctx] = UndefinedValue(BitWidth).asExpr(type)
                    return
                }

                exprs[ctx] = when (operator) {
                    "==" -> op1 isEqualTo op2
                    "!=" -> op1 isNotEqualTo op2
                    else -> error("Unknown operand!")
                }.asExpr(type)
            }
        }
    }

    fun logical(ctx: ParserRuleContext, children: List<T>, operator: String) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        if (children.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(children) ?: return

        val op1 = exprs[children[0]]?.value ?: return
        val op2 = exprs[children[1]]?.value ?: return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            exprs[ctx] = UndefinedValue(BitWidth).asExpr(type)
            return
        }

        op1 as SimpleValue
        op2 as SimpleValue

        exprs[ctx] = when (operator) {
            "||" -> op1.isTrue() or op2.isTrue()
            "&&" -> op1.isTrue() and op2.isTrue()
            else -> error("Unknown operand!")
        }.asExpr(type)
    }

    fun ternary(ctx: ParserRuleContext, children: List<T>) {
        if (canSkip(ctx)) return

        if (children.size != 3) return

        widthFence[children[0]] = true

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(children) ?: return

        val cond = exprs[children[0]]?.value ?: return
        val op1 = exprs[children[1]]?.value ?: return
        val op2 = exprs[children[2]]?.value ?: return

        val op1Width = op1.width
        val op2Width = op2.width

        if (!cond.width.isSimple()) {
            context.reportError(children[0], ErrorStrings.TERN_SELECTOR_MULTI_DIM)
            return
        }

        if (!context.checkSimpleOrCompatible(children[1], children[2]) {
                context.reportError(it, ErrorStrings.OP_TERN_DIM_MISMATCH)
            }) return

        val width = op1Width.mergeWith(op2Width)

        if (cond is UndefinedValue) {
            exprs[ctx] = UndefinedValue(width).asExpr(type)
            return
        }

        val value = if (cond.isTrue().lsb == Bit.B1) op1 else op2
        if (value.width != width) {
            exprs[ctx] = value.resizeToMatch(width).asExpr(type)
        } else {
            exprs[ctx] = value.asExpr(type)
        }
    }

    private fun SignalWidth.canConcat(other: SignalWidth): Boolean = when (this) {
        is ArrayWidth -> other is ArrayWidth && next.isCompatibleWith(other.next)
        is SimpleWidth -> other is SimpleWidth
        is StructWidth -> false
    }

    /**
     * Merges two compatible widths into one replacing [UndefinedWidth] with defined ones where possible.
     */
    private fun SignalWidth.mergeWith(other: SignalWidth): SignalWidth {
        require(this.canAssign(other)) { "mergeWith() can only be used on assignable widths!" }
        return when (this) {
            is DefinedArrayWidth -> copy(next = next.mergeWith((other as ArrayWidth).next))
            is BitWidth -> when (other) {
                is BitWidth, is UndefinedSimpleWidth -> BitWidth
                is BitListWidth -> other
                else -> error("[BUG] Impossible case if compatible!")
            }

            is BitListWidth -> BitListWidth(size.coerceAtLeast(other.bitCount ?: 0))
            is StructWidth -> this
            is UndefinedArrayWidth -> when (other) {
                is DefinedArrayWidth -> other.copy(next = next.mergeWith(other.next))
                is UndefinedArrayWidth -> UndefinedArrayWidth(next.mergeWith(other.next))
                else -> error("[BUG] Impossible case if compatible!")
            }

            is UndefinedSimpleWidth -> when (other) {
                is BitWidth, is BitListWidth, is UndefinedSimpleWidth -> other
                else -> error("[BUG] Impossible case if compatible!")
            }
        }
    }
}
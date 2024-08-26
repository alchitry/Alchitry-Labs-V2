package com.alchitry.labs2.parsers.hdl.verilog.parsers

import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.grammar.VerilogParser.Constant_expressionContext
import com.alchitry.labs2.parsers.grammar.VerilogParserBaseListener
import com.alchitry.labs2.parsers.hdl.ExprEvaluator
import com.alchitry.labs2.parsers.hdl.asConstExpr
import com.alchitry.labs2.parsers.hdl.asExpr
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprContext

class ConstantExprParser(
    private val context: VerilogExprContext,
    evaluator: ExprEvaluator<Constant_expressionContext>? = null
) : VerilogParserBaseListener() {
    private val evaluator = evaluator?.withContext(context) ?: ExprEvaluator(context)

    fun resolve(ctx: Constant_expressionContext) = evaluator.resolve(ctx)

    private fun String.toIntIgnoreUnderscore() = this.replace("_", "").toInt()
    private fun String.sanitizeNumber() = this.replace("_", "").replace("?", "z")

    override fun exitNumber(ctx: VerilogParser.NumberContext) {
        evaluator.passThrough(ctx, ctx.getChild(0))
    }

    override fun exitUnsigned_number(ctx: VerilogParser.Unsigned_numberContext) {
        evaluator.setExpr(
            ctx, BitListValue(
                ctx.text.sanitizeNumber(),
                signed = false
            ).asConstExpr()
        )
    }

    override fun exitDecimal_number(ctx: VerilogParser.Decimal_numberContext) {
        ctx.unsigned_number()?.let {
            evaluator.passThrough(ctx, it)
            return
        }

        val size = ctx.size()?.text?.toIntIgnoreUnderscore()
        val signed = ctx.decimal_base()?.text?.contains('s', ignoreCase = true) ?: false
        val value = when {
            ctx.decimal_value()?.UNSIGNED_NUMBER() != null -> {
                BitListValue(
                    ctx.decimal_value()!!.UNSIGNED_NUMBER()!!.text.sanitizeNumber(),
                    10,
                    width = size,
                    signed = signed
                )
            }

            ctx.decimal_value()?.X_OR_Z_UNDERSCORE() != null -> {
                BitListValue(
                    ctx.decimal_value()!!.X_OR_Z_UNDERSCORE()!!.text.sanitizeNumber(),
                    2,
                    width = size,
                    signed = signed
                )
            }

            else -> {
                return
            }
        }

        evaluator.setExpr(ctx, value.asConstExpr())
    }

    override fun exitOctal_number(ctx: VerilogParser.Octal_numberContext) {
        evaluator.setExpr(
            ctx,
            BitListValue(
                ctx.octal_value()?.text?.sanitizeNumber() ?: return,
                radix = 8,
                width = ctx.size()?.text?.toIntIgnoreUnderscore(),
                signed = ctx.octal_base()?.text?.contains('s', ignoreCase = true) ?: false
            ).asConstExpr()
        )
    }

    override fun exitBinary_number(ctx: VerilogParser.Binary_numberContext) {
        evaluator.setExpr(
            ctx,
            BitListValue(
                ctx.binary_value()?.text?.sanitizeNumber() ?: return,
                radix = 2,
                width = ctx.size()?.text?.toIntIgnoreUnderscore(),
                signed = ctx.binary_base()?.text?.contains('s', ignoreCase = true) ?: false
            ).asConstExpr()
        )
    }

    override fun exitHex_number(ctx: VerilogParser.Hex_numberContext) {
        evaluator.setExpr(
            ctx,
            BitListValue(
                ctx.hex_value()?.text?.sanitizeNumber() ?: return,
                radix = 16,
                width = ctx.size()?.text?.toIntIgnoreUnderscore(),
                signed = ctx.hex_base()?.text?.contains('s', ignoreCase = true) ?: false
            ).asConstExpr()
        )
    }

    override fun exitReal_number(ctx: VerilogParser.Real_numberContext) {
        context.reportError(ctx, "Real numbers aren't supported.")
    }

    override fun exitConstPrimaryNumber(ctx: VerilogParser.ConstPrimaryNumberContext) {
        evaluator.passThrough(ctx, ctx.number())
    }

    override fun exitConstPrimaryIdentifier(ctx: VerilogParser.ConstPrimaryIdentifierContext) {
        if (ctx.constant_range_expression() != null) {
            context.reportError(ctx, "Signal selection isn't supported yet.")
            return
        }

        val signal = context.resolveSignal(ctx, ctx.identifier()?.text ?: return)
        if (signal !is Signal) {
            context.reportError(ctx, "Signal \"${ctx.identifier()?.text}\" is not a fully qualified signal.")
            return
        }
        evaluator.setExpr(ctx, signal.read().asExpr(signal.type))
    }

    override fun exitConstPrimaryConcatenation(ctx: VerilogParser.ConstPrimaryConcatenationContext) {
        evaluator.concatenate(ctx, ctx.constant_concatenation()?.constant_expression() ?: emptyList())
    }

    override fun exitConstPrimaryMultipleConcatenation(ctx: VerilogParser.ConstPrimaryMultipleConcatenationContext) {
        val duplication = ctx.constant_multiple_concatenation()?.constant_expression() ?: return
        val value = ctx.constant_multiple_concatenation()?.constant_concatenation() ?: return
        evaluator.duplicate(ctx, listOf(duplication, value))
    }

    override fun exitConstPrimaryFunctionCall(ctx: VerilogParser.ConstPrimaryFunctionCallContext) {
        context.reportError(ctx, "Function calls aren't supported.") // TODO
    }

    override fun exitConstPrimarySystemFunctionCall(ctx: VerilogParser.ConstPrimarySystemFunctionCallContext) {
        context.reportError(ctx, "System function calls aren't supported.")
    }

    override fun exitConstPrimaryGroup(ctx: VerilogParser.ConstPrimaryGroupContext) {
        val expr = ctx.constant_mintypmax_expression() ?: return
        if (expr.constant_expression().size > 1) {
            context.reportError(ctx, "Min:Typ:Max expressions aren't supported here.")
        }
        evaluator.passThrough(ctx, expr.constant_expression(0))
    }

    override fun exitConstPrimaryString(ctx: VerilogParser.ConstPrimaryStringContext) {
        context.reportError(ctx, "Strings aren't supported.") // TODO
    }

    override fun exitConstExprPrimary(ctx: VerilogParser.ConstExprPrimaryContext) {
        evaluator.passThrough(ctx, ctx.constant_primary())
    }

    override fun exitConstExprUnary(ctx: VerilogParser.ConstExprUnaryContext) {
        when (val operator = ctx.unary_operator()?.text ?: return) {
            "+" -> evaluator.passThrough(ctx, ctx.constant_primary())
            "-" -> evaluator.negate(ctx, ctx.constant_primary())
            "!", "~" -> evaluator.invert(ctx, ctx.constant_primary(), operator)
            "&", "|", "^", "~&", "~^", "~|", "^~" -> evaluator.reduction(ctx, ctx.constant_primary(), operator)
            else -> context.reportError(ctx, "Unexpected operator: $operator")
        }
    }

    override fun exitConstExprExpo(ctx: VerilogParser.ConstExprExpoContext) {
        context.reportError(ctx, "** operator isn't currently supported.") // TODO
    }

    override fun exitConstExprMulDivRem(ctx: VerilogParser.ConstExprMulDivRemContext) {
        val operator = ctx.getChild(1)?.text ?: return
        if (operator == "%") {
            context.reportError(ctx, "Remainder expressions aren't supported.") // TODO
            return
        }
        evaluator.multiplyOrDivide(ctx, ctx.constant_expression(), operator)
    }

    override fun exitConstExprAddSub(ctx: VerilogParser.ConstExprAddSubContext) {
        evaluator.addOrSubtract(ctx, ctx.constant_expression(), ctx.getChild(1)?.text ?: return)
    }

    override fun exitConstExprShift(ctx: VerilogParser.ConstExprShiftContext) {
        evaluator.shift(ctx, ctx.constant_expression(), ctx.getChild(1)?.text ?: return)
    }

    override fun exitConstExprCompare(ctx: VerilogParser.ConstExprCompareContext) {
        evaluator.comparison(ctx, ctx.constant_expression(), ctx.getChild(1)?.text ?: return)
    }

    override fun exitConstExprEquality(ctx: VerilogParser.ConstExprEqualityContext) {
        evaluator.comparison(ctx, ctx.constant_expression(), ctx.getChild(1)?.text ?: return)
    }

    override fun exitConstExprAnd(ctx: VerilogParser.ConstExprAndContext) {
        evaluator.bitwise(ctx, ctx.constant_expression(), "&")
    }

    override fun exitConstExprXor(ctx: VerilogParser.ConstExprXorContext) {
        if (ctx.getChild(1)?.text?.contains("~") == true) {
            context.reportError(ctx, "Xnor expressions aren't supported.") // TODO
            return
        }
        evaluator.bitwise(ctx, ctx.constant_expression(), "^")
    }

    override fun exitConstExprOr(ctx: VerilogParser.ConstExprOrContext) {
        evaluator.bitwise(ctx, ctx.constant_expression(), "|")
    }

    override fun exitConstExprLogicalOr(ctx: VerilogParser.ConstExprLogicalOrContext) {
        evaluator.logical(ctx, ctx.constant_expression(), "||")
    }

    override fun exitConstExprLogicalAnd(ctx: VerilogParser.ConstExprLogicalAndContext) {
        evaluator.logical(ctx, ctx.constant_expression(), "&&")
    }

    override fun exitConstExprTernary(ctx: VerilogParser.ConstExprTernaryContext) {
        evaluator.ternary(ctx, ctx.constant_expression())
    }
}
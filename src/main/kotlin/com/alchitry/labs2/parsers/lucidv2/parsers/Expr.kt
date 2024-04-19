package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.lucidv2.values.Value

enum class ExprType(val known: Boolean) {
    /** Value changes at runtime. */
    Dynamic(false),

    /** Value changes but known during synthesis. */
    Known(true),

    /** Value never changes. */
    Constant(true);
}

data class Expr(
    val value: Value,
    val type: ExprType,
)

// syntax sugar

/**
 * Constructs an [Expr] with the given value and marked as [ExprType.Constant].
 *
 * @param value The value of the constant expression.
 * @return An `Expr` object representing a constant expression.
 */
fun ConstExpr(value: Value) = Expr(value, ExprType.Constant)

/**
 * Constructs an [Expr] with the given value and marked as [ExprType.Known].
 *
 * @param value The value of the known expression.
 * @return An `Expr` object representing a known expression.
 */
fun KnownExpr(value: Value) = Expr(value, ExprType.Known)

/**
 * Constructs an [Expr] with the given value and marked as [ExprType.Dynamic].
 *
 * @param value The value of the dynamic expression.
 * @return An `Expr` object representing a dynamic expression.
 */
fun DynamicExpr(value: Value) = Expr(value, ExprType.Dynamic)

/**
 * Converts this [Value] to an [Expr] marked as [ExprType.Constant].
 */
fun Value.asConstExpr() = ConstExpr(this)

/**
 * Converts this [Value] to an [Expr] marked as [ExprType.Known].
 */
fun Value.asKnownExpr() = KnownExpr(this)

/**
 * Converts this [Value] to an [Expr] marked as [ExprType.Dynamic].
 */
fun Value.asDynamicExpr() = DynamicExpr(this)

/**
 * Converts this [Value] to an [Expr] marked as [type].
 */
fun Value.asExpr(type: ExprType) = Expr(this, type)
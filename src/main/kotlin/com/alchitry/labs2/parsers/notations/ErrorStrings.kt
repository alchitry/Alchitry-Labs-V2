package com.alchitry.labs2.parsers.notations

// TODO: Replace these inline
object ErrorStrings {
    const val ARRAY_BUILDING_DIM_MISMATCH = "Each element in an array builder must have the same dimensions"
    const val ARRAY_NOT_DIVISIBLE = "The array \"%s\" can't be split evenly"
    const val SHIFT_MULTI_DIM = "Only single dimensional arrays can be shifted"
    const val BUILD_MULTI_DIM = "Only single dimensional arrays can be built"
    const val SIGNED_MULTI_DIM = "Only single dimensional values can use \$signed()"
    const val UNSIGNED_MULTI_DIM = "Only single dimensional values can use \$unsigned()"
    const val TERN_SELECTOR_MULTI_DIM = "Only single dimensional arrays can be used as the selector"
    const val OP_DIM_MISMATCH = "When performing an %s operation the argument's dimensions must match"
    const val OP_TERN_DIM_MISMATCH = "The dimensions of both results must match for the ternary operator"
    const val TRUNC_WARN = "The signal \"%s\" is wider than \"%s\" and the most significant bits will be dropped"
    const val UNKNOWN_FUNCTION = "The function \"%s\" is unknown"
    const val CONST_FUNCTION = "The function \"%s\" can only be used on constants"
    const val FUNCTION_ARG_NAN = "The argument \"%s\" with value \"%s\" must be a number"
    const val FUNCTION_ARG_COUNT = "The function \"%s\" takes exactly %d argument(s)"
    const val FUNCTION_MIN_ARG_COUNT = "The function \"%s\" requires at least %d argument(s)"
    const val FUNCTION_NOT_FLAT = "The function \"%s\" can't be used on multidimensional arrays or structs"
    const val FUNCTION_ARG_ZERO = "The argument \"%s\" can't be zero"
    const val FUNCTION_ARG_NEG = "The argument \"%s\" can't be negative"
    const val FUNCTION_ARG_NOT_ARRAY = "The argument \"%s\" isn't an array"
    const val VALUE_BIGGER_THAN_INT = "The value %s can't fit into 32bits"
    const val UNKNOWN_WIDTH = "The width of the \"%s\" couldn't be determined but is required!"
}

object WarningStrings {
    const val DIVIDE_NOT_POW_2 = "The denominator isn't a constant power of 2. This operation can be very slow and may cause timing errors."
}
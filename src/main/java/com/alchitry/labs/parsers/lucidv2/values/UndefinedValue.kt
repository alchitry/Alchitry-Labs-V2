package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

data class UndefinedValue(
    override val constant: Boolean,
    val width: SignalWidth = UndefinedSimpleWidth
) : Value() {
    override fun isNumber() = false

    override fun asMutable() = copy(constant = false)
    override fun withSign(signed: Boolean) = this

    override val signalWidth = width

    override fun invert() = this

    override fun isTrue() = BitValue(Bit.Bx, constant, false)

    override infix fun and(other: Value): Value = when (other) {
        is ArrayValue -> TODO()
        is BitListValue -> TODO()
        is BitValue -> TODO()
        is StructValue -> TODO()
        is UndefinedValue -> TODO()
    }

    override fun or(other: Value): Value = when (other) {
        is ArrayValue -> TODO()
        is BitListValue -> TODO()
        is BitValue -> TODO()
        is StructValue -> TODO()
        is UndefinedValue -> TODO()
    }

    override fun xor(other: Value): Value = when (other) {
        is ArrayValue -> TODO()
        is BitListValue -> TODO()
        is BitValue -> TODO()
        is StructValue -> TODO()
        is UndefinedValue -> TODO()
    }

    override fun reverse(): UndefinedValue =
        if (width.isArray()) this else error("reverse() can't be called on UndefinedValues that aren't arrays.")

    override fun resizeToMatch(newWidth: SignalWidth): UndefinedValue {
        check(width.canAssign(newWidth)) { "UndefinedValue width $width could not be resized to new width of $newWidth!" }
        return copy(width = newWidth)
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): Value {
        error("Write isn't supported on undefined values!")
    }
}

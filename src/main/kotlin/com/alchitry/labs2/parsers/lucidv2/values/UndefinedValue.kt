package com.alchitry.labs2.parsers.lucidv2.values

import com.alchitry.labs2.parsers.lucidv2.types.SignalSelector

/**
 * Used to represent values that WILL be defined but currently aren't.
 * This should only happen from parameters without a default or test value and any values derived from them.
 */
data class UndefinedValue(
    override val constant: Boolean,
    override val width: SignalWidth = UndefinedSimpleWidth()
) : Value() {
    override fun isNumber() = false

    override fun asMutable() = copy(constant = false)
    override fun withSign(signed: Boolean) = this

    override fun invert() = this

    override fun isTrue() = BitValue(Bit.Bx, constant, false)

    override fun where(bit: Bit) = this.width.filledWith(Bit.B0, constant, false)
    override fun replace(mask: Value, bit: Bit) = this

    override fun toString(format: ValueFormat): String = "UndefinedValue($width)"

    fun selectBits(selection: SignalSelector.Bits): UndefinedValue {
        if (width !is SimpleWidth)
            return this
        if (selection.count == 1)
            return UndefinedValue(constant, BitWidth)
        return UndefinedValue(constant, BitListWidth(selection.count))
    }

    override infix fun and(other: Value): UndefinedValue {
        require(other is SimpleValue || other is UndefinedValue) { "And can only be performed with Undefined or Simple values." }
        return UndefinedValue(other.constant && constant)
    }

    override fun or(other: Value): UndefinedValue {
        require(other is SimpleValue || other is UndefinedValue) { "Or can only be performed with Undefined or Simple values." }
        return UndefinedValue(other.constant && constant)
    }

    override fun xor(other: Value): UndefinedValue {
        require(other is SimpleValue || other is UndefinedValue) { "Xor can only be performed with Undefined or Simple values." }
        return UndefinedValue(other.constant && constant)
    }

    override fun reverse(): UndefinedValue =
        if (this.width.isArray()) this else error("reverse() can't be called on UndefinedValues that aren't arrays.")

    override fun resizeToMatch(newWidth: SignalWidth): UndefinedValue {
        check(this.width.canAssign(newWidth)) { "UndefinedValue width ${this.width} could not be resized to new width of $newWidth!" }
        return copy(width = newWidth)
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): Value {
        error("Write isn't supported on undefined values!")
    }
}

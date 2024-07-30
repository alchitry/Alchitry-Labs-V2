package com.alchitry.labs2.parsers.hdl.lucidv2.values

import com.alchitry.labs2.parsers.hdl.types.SignalSelector


data class BitValue(
    val bit: Bit,
    override val signed: Boolean
) : SimpleValue(signed) {
    override val size: Int = 1
    override val msb: Bit = bit
    override val lsb: Bit = bit
    override val bits: List<Bit> = listOf(bit)

    private fun doOp(other: BitValue, value: Bit) = BitValue(
        value,
        signed && other.signed
    )

    infix fun and(other: BitValue) = doOp(other, bit and other.bit)
    infix fun or(other: BitValue) = doOp(other, bit or other.bit)
    infix fun xor(other: BitValue) = doOp(other, bit xor other.bit)

    override fun toString(format: ValueFormat): String = bit.char.toString()

    override fun where(bit: Bit) = copy(bit = if (this.bit == bit) Bit.B1 else Bit.B0)
    override fun replace(mask: Value, bit: Bit): Value {
        require(mask is BitValue)
        return copy(bit = if (mask.bit == Bit.B1) bit else this.bit)
    }

    override fun isNumber(): Boolean = bit.isNumber()

    override fun withSign(signed: Boolean) = copy(signed = signed)

    override val width = BitWidth

    override fun replaceBit(old: Bit, new: Bit) = copy(bit = if (bit == old) new else bit)

    override fun invert() = copy(bit = !bit)
    override fun reverse() = this

    override fun isTrue() = BitValue(
        when (bit) {
            Bit.B0 -> Bit.B0
            Bit.B1 -> Bit.B1
            Bit.Bx, Bit.Bz -> Bit.Bx
        },
        false
    )

    override fun toString(): String {
        return super.toString()
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): BitValue {
        require(selection.isEmpty()) { "Attempted to select further than a single bit!" }
        require(newValue is BitValue) { "Attempted to write a value other than a single bit to a BitValue!" }
        return copy(bit = newValue.bit)
    }
}
package com.alchitry.labs.parsers.lucidv2.values

data class BitValue(
    val bit: Bit,
    override val constant: Boolean,
    val signed: Boolean
) : Value() {
    private fun doOp(other: BitValue, value: Bit) = BitValue(
        value,
        constant && other.constant,
        signed && other.signed
    )

    infix fun and(other: BitValue) = doOp(other, bit and other.bit)
    infix fun or(other: BitValue) = doOp(other, bit or other.bit)
    infix fun xor(other: BitValue) = doOp(other, bit xor other.bit)

    override fun isNumber(): Boolean = bit.isNumber()

    override fun asMutable() = copy(constant = false)

    override val signalWidth = BitWidth

    override fun invert() = copy(bit = !bit)

    override fun isTrue() = BitValue(
        when (bit) {
            Bit.B0 -> Bit.B0
            Bit.B1 -> Bit.B1
            Bit.Bx, Bit.Bz -> Bit.Bx
            Bit.Bu -> Bit.Bu
        },
        constant,
        false
    )
}
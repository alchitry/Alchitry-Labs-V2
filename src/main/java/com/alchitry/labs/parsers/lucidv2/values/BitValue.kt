package com.alchitry.labs.parsers.lucidv2.values

data class  BitValue(
    val bit: Bit,
    override val constant: Boolean,
    override val signed: Boolean
) : SimpleValue(constant, signed) {
    override val size: Int = 1
    override val msb: Bit = bit
    override val lsb: Bit = bit
    override val bits: List<Bit> = listOf(bit)

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
    override fun withSign(signed: Boolean) = copy(signed = signed)

    override val signalWidth = BitWidth

    override fun invert() = copy(bit = !bit)
    override fun reverse() = this

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

    override fun toString(): String {
        val sb = StringBuilder()
        if (constant)
            sb.append("const ")
        if (signed)
            sb.append("signed ")
        sb.append('{')
        sb.append(bit.toString().substring(1))
        sb.append('}')
        return sb.toString()
    }
}
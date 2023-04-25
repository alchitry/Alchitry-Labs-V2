package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

data class BitListValue(
    override val bits: List<Bit>,
    override val constant: Boolean,
    override val signed: Boolean
) : SimpleValue(constant, signed), List<Bit> by bits {
    constructor(size: Int, constant: Boolean, signed: Boolean, init: (Int) -> Bit) : this(
        List(size, init),
        constant,
        signed
    )

    override val lsb: Bit = bits.first()
    override val msb: Bit = bits.last()

    override fun asMutable(): BitListValue = copy(constant = false)

    override val signalWidth: SimpleWidth = SimpleWidth(bits.size)

    override fun invert(): BitListValue = copy(bits = bits.map { !it })
    override fun reverse(): BitListValue = copy(bits = bits.reversed())

    fun resize(width: Int, signExtend: Boolean = signed): BitListValue {
        if (width == size)
            return copy(signed = signExtend)
        if (width < size)
            return BitListValue(subList(0, width), constant, signExtend)
        val extendBit = if (!signExtend && msb == Bit.B1) Bit.B0 else msb
        val newBits = bits.toMutableList()
        repeat(width - size) { newBits.add(extendBit) }
        return BitListValue(newBits, constant, signExtend)
    }

    /** Changes sign without changing bits */
    fun setSign(signed: Boolean): BitListValue = copy(signed = signed)

    fun getBit(idx: Int): BitValue = BitValue(bits[idx], constant, signed)

    fun selectBits(selection: SignalSelector.Bits): Value {
        try {
            val newBits = bits.subList(selection.range.first, selection.range.last + 1)
            if (newBits.size == 1)
                return BitValue(newBits.first(), constant, signed)
            return copy(bits = newBits)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(
                selection,
                "Selection $selection is outside the bounds of [${bits.size - 1}:0]"
            )
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): BitListValue {
        return BitListValue(bits.subList(fromIndex, toIndex), constant, signed)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (constant)
            sb.append("const ")
        if (signed)
            sb.append("signed ")
        sb.append('{')
        for (i in bits.indices.reversed()) {
            val bv = bits[i]
            sb.append(bv.toString().substring(1))
        }
        sb.append('}')
        return sb.toString()
    }
}
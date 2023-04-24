package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

data class BitListValue(
    override val bits: List<Bit>,
    override val constant: Boolean,
    override val signed: Boolean
) : SimpleValue(constant, signed), List<Bit> by bits {
    constructor(constant: Boolean, signed: Boolean, size: Int, init: (Int) -> Bit) : this(
        List(size, init),
        constant,
        signed
    )

    override val lsb: Bit = bits.first()
    override val msb: Bit = bits.last()

    override fun asMutable(): BitListValue = copy(constant = false)

    override val signalWidth: SimpleWidth = SimpleWidth(bits.size)

    override fun invert(): BitListValue = copy(bits = bits.map { !it })

    fun resize(width: Int, signExtend: Boolean = signed): BitListValue {
        if (width == size)
            return copy(signed = signExtend)
        if (width < size)
            return BitListValue(subList(0, width), signExtend, constant)
        val extendBit = if (!signExtend && msb == Bit.B1) Bit.B0 else msb
        val newBits = bits.toMutableList()
        repeat(width - size) { newBits.add(extendBit) }
        return BitListValue(newBits, constant, signExtend)
    }

    /** Changes sign without changing bits */
    fun setSign(signed: Boolean): BitListValue = copy(signed = signed)



    fun select(selection: SignalSelector.Bits): Value {
        try {
            val newBits = bits.subList(selection.range.first, selection.range.last + 1)
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
}
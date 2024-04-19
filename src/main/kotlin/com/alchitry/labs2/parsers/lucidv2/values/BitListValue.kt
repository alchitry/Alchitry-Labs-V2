package com.alchitry.labs2.parsers.lucidv2.values

import com.alchitry.labs2.parsers.lucidv2.types.SignalSelectionException
import com.alchitry.labs2.parsers.lucidv2.types.SignalSelector


data class BitListValue(
    override val bits: List<Bit>,
    override val signed: Boolean
) : SimpleValue(signed), List<Bit> by bits {
    constructor(size: Int, signed: Boolean, init: (Int) -> Bit) : this(
        List(size, init),
        signed
    )

    override fun toString(format: ValueFormat): String = when (format) {
        ValueFormat.Binary -> buildString {
            bits.asReversed().forEach { append(it.char) }
        }

        ValueFormat.Decimal -> toBigInt()?.toString() ?: "NaN"

        is ValueFormat.Fractional -> toBigInt()?.toBigDecimal()?.divide(2f.toBigDecimal().pow(format.fractionalBits))
            ?.toString() ?: "NaN"

        ValueFormat.Hex -> buildString {
            bits.chunked(4).asReversed().forEach {
                val value = BitListValue(it, false)
                append(
                    when {
                        value.isNumber() -> value.toBigInt()?.toString(16) ?: "?"
                        value.contains(Bit.Bx) -> "x"
                        value.contains(Bit.Bz) -> "z"
                        else -> "?"
                    }
                )
            }
        }
    }

    override val lsb: Bit = bits.firstOrNull() ?: Bit.Bx
    override val msb: Bit = bits.lastOrNull() ?: Bit.Bx

    override fun withSign(signed: Boolean): BitListValue = copy(signed = signed)

    override val width: DefinedSimpleWidth = BitListWidth(bits.size)

    override fun replaceBit(old: Bit, new: Bit) = copy(bits = bits.map { if (it == old) new else it })
    override fun invert(): BitListValue = copy(bits = bits.map { !it })
    override fun reverse(): BitListValue = copy(bits = bits.reversed())

    override fun where(bit: Bit) = copy(bits = bits.map { if (it == bit) Bit.B1 else Bit.B0 })

    override fun replace(mask: Value, bit: Bit): Value {
        require(mask is BitListValue && mask.bits.size == bits.size)
        return copy(bits = bits.mapIndexed { index, v -> if (mask[index] == Bit.B1) bit else v })
    }

    fun resize(width: Int, signExtend: Boolean = signed): BitListValue {
        if (width == size)
            return copy(signed = signExtend)
        if (width < size)
            return BitListValue(subList(0, width), signExtend)
        val extendBit = if (!signExtend && msb == Bit.B1) Bit.B0 else msb
        val newBits = bits.toMutableList()
        repeat(width - size) { newBits.add(extendBit) }
        return BitListValue(newBits, signExtend)
    }

    /** Changes sign without changing bits */
    fun setSign(signed: Boolean): BitListValue = copy(signed = signed)

    fun getBit(idx: Int): BitValue = BitValue(bits[idx], false)

    fun selectBits(selection: SignalSelector.Bits): Value {
        try {
            val newBits = bits.subList(selection.range.first, selection.range.last + 1)
            if (newBits.size == 1)
                return BitValue(newBits.first(), false)
            return copy(bits = newBits)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(
                selection,
                "Selection $selection is outside the bounds of [${bits.size - 1}:0]"
            )
        }
    }

    /**
     * Returns the minimum number of bits that can represent this value.
     */
    fun minimumBits(): Int {
        val searchBit = if (signed && msb == Bit.B1) Bit.B0 else Bit.B1
        val offset = bits.reversed().indexOfFirst { it == searchBit || it == Bit.Bx }
        if (offset < 0)
            return 1
        return size - offset + if (signed) 1 else 0
    }

    override fun subList(fromIndex: Int, toIndex: Int): BitListValue {
        return BitListValue(bits.subList(fromIndex, toIndex), false)
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): Value {
        if (newValue is UndefinedValue) {
            return UndefinedValue(width)
        }

        if (selection.isEmpty()) {
            require(newValue is BitListValue && newValue.width == width) {
                "Attempted to assign an incompatible type to the BitListValue!"
            }
            return copy(bits = newValue.bits)
        }

        require(selection.size == 1) {
            "Selection contains more than one selector for a BitListValue!"
        }

        when (val selector = selection.first()) {
            is SignalSelector.Bits -> {
                val selectedCt = selector.range.last - selector.range.first + 1
                if (selectedCt == 1) {
                    require(newValue is BitValue) {
                        "Attempted to assign a value other than a single bit to a single bit in a BitListValue!"
                    }
                    return copy(
                        bits = List(bits.size) {
                            if (selector.range.contains(it))
                                newValue.bit
                            else
                                bits[it]
                        }
                    )

                } else {
                    require(newValue is BitListValue) {
                        "Attempted to assign a value other than a BitListValue to multiple elements in a BitListValue!"
                    }
                    require(newValue.bits.size == selectedCt) {
                        "Selected bit count doesn't match the newValue bit count!"
                    }
                    return copy(
                        bits = List(bits.size) {
                            if (selector.range.contains(it))
                                newValue.bits[it - selector.range.first]
                            else
                                bits[it]
                        }
                    )
                }
            }

            is SignalSelector.Struct -> error("Attempted to used struct selection on BitListValue!")
        }
    }
}
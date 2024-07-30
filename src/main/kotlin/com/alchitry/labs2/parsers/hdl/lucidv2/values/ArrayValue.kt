package com.alchitry.labs2.parsers.hdl.lucidv2.values

import com.alchitry.labs2.parsers.hdl.types.SignalSelectionException
import com.alchitry.labs2.parsers.hdl.types.SignalSelector


data class ArrayValue(
    val elements: List<Value>
) : Value(), List<Value> by elements {
    init {
        require(elements.none { it is BitValue }) { "ArrayValue should never have BitValue elements!" }
        if (elements.isNotEmpty()) {
            val base = elements.first().width
            require(elements.all { value ->
                base.isCompatibleWith(value.width)
            }) { "ArrayValue elements must all be the same size!" }
        }
    }

    override fun toString(format: ValueFormat): String = joinToString(", ", "[", "]") {
        it.toString(format)
    }

    override fun isNumber(): Boolean = elements.all { it.isNumber() }

    override fun withSign(signed: Boolean): ArrayValue = copy(elements = elements.map { it.withSign(signed) })

    override val width: DefinedArrayWidth =
        DefinedArrayWidth(
            elements.size,
            elements.firstOrNull { it.width !is UndefinedSimpleWidth }?.width ?: UndefinedSimpleWidth()
        )

    override fun invert(): ArrayValue = copy(elements = elements.map { it.invert() })

    override fun isTrue(): BitValue = BitListValue(elements.map { it.isTrue() }, signed = false).isTrue()

    override fun where(bit: Bit) = copy(elements = elements.map { it.where(bit) })

    override fun replace(mask: Value, bit: Bit): ArrayValue {
        require(mask is ArrayValue && mask.elements.size == elements.size)
        return copy(elements = elements.mapIndexed { index, value -> value.replace(mask[index], bit) })
    }

    override fun replaceBit(old: Bit, new: Bit) = copy(elements = map { it.replaceBit(old, new) })

    override fun and(other: Value): ArrayValue {
        require(other is ArrayValue) { "ArrayValue can only be and'd with another ArrayValue!" }
        require(other.size == size) { "And operations on ArrayValues must have matching sizes!" }
        return ArrayValue(mapIndexed { index, value -> other[index] and value })
    }

    override fun or(other: Value): ArrayValue {
        require(other is ArrayValue) { "ArrayValue can only be or'd with another ArrayValue!" }
        require(other.size == size) { "Or operations on ArrayValues must have matching sizes!" }
        return ArrayValue(mapIndexed { index, value -> other[index] or value })
    }

    override fun xor(other: Value): ArrayValue {
        require(other is ArrayValue) { "ArrayValue can only be xor'd with another ArrayValue!" }
        require(other.size == size) { "Xor operations on ArrayValues must have matching sizes!" }
        return ArrayValue(mapIndexed { index, value -> other[index] xor value })
    }

    override fun reverse() = ArrayValue(reversed())

    override fun resizeToMatch(newWidth: SignalWidth): ArrayValue {
        require(width.isCompatibleWith(newWidth)) { "Cannot resize ArrayValue to match other value" }
        return this
    }

    fun selectElements(selection: SignalSelector.Bits): Value {
        try {
            val newElements = elements.subList(selection.range.first, selection.range.last + 1)
            if (newElements.size == 1)
                return newElements.first()
            return copy(elements = newElements)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(
                selection,
                "Selection $selection is outside the bounds of [${elements.size - 1}:0]"
            )
        }
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): ArrayValue {
        if (selection.isEmpty()) {
            if (newValue !is ArrayValue || newValue.width != width) {
                error("Incompatible newValue width!")
            }
            return newValue
        }
        when (val selector = selection.first()) {
            is SignalSelector.Bits -> {
                val selectedCt = selector.range.last - selector.range.first + 1
                if (selectedCt == 1) {
                    return copy(elements = List(elements.size) {
                        if (it == selector.range.first)
                            elements[it].write(selection.subList(1, selection.size), newValue)
                        else
                            elements[it]
                    })
                } else {
                    if (newValue !is ArrayValue)
                        error("Selection is multiple elements but newValue is not an ArrayValue!")
                    if (newValue.elements.size != selectedCt)
                        error("The selection size doesn't match the newValue size!")
                    return copy(elements = List(elements.size) {
                        if (selector.range.contains(it))
                            newValue.elements[it - selector.range.first]
                        else
                            elements[it]
                    })
                }
            }

            is SignalSelector.Struct -> error("Can't use struct selection on an array!")
        }
    }
}
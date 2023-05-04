package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

data class ArrayValue(
    val elements: List<Value>
) : Value(), List<Value> by elements {
    init {
        require(elements.none { it is BitValue }) { "ArrayValue should never have BitValue elements!" }
        if (elements.isNotEmpty())
            require(elements.all { value -> value::class == elements.first()::class }) { "ArrayValue elements must all be the same class!" }
    }

    override val constant: Boolean
        get() = all { it.constant }

    override fun isNumber(): Boolean = elements.all { it.isNumber() }

    override fun asMutable(): ArrayValue = copy(elements = elements.map { it.asMutable() })

    override fun withSign(signed: Boolean): ArrayValue = copy(elements = elements.map{ it.withSign(signed) })

    override val signalWidth: ArrayWidth = ArrayWidth(elements.size, elements[0].signalWidth)

    override fun invert(): ArrayValue = copy(elements = elements.map { it.invert() })

    override fun isTrue(): BitValue = BitListValue(elements.map { it.isTrue() }, signed = false).isTrue()

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
        require(signalWidth == newWidth) { "Cannot resize ArrayValue to match other value" }
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
}
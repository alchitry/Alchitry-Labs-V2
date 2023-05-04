package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector
import com.alchitry.labs.parsers.lucidv2.signals.StructType

data class StructValue(
    val type: StructType,
    private val valueMap: Map<String, Value>,
) : Value(), Map<String, Value> by valueMap {
    init {
        check(valueMap.keys.containsAll(type.keys)) { "A StructValue's value must contain all the keys in the StructType!" }
        check(type.keys.containsAll(valueMap.keys)) { "A StructValue's value must contain only the keys in the StructType!" }
    }

    override val constant: Boolean
        get() = valueMap.values.all { it.constant }

    override fun isNumber(): Boolean = values.all { it.isNumber() }

    override fun asMutable(): StructValue = copy(valueMap = mapValues { it.value.asMutable() })

    /** withSign() does nothing to struct values since the members have explicit signs */
    override fun withSign(signed: Boolean) = this

    override val signalWidth: StructWidth = StructWidth(type)

    override fun invert(): StructValue = copy(valueMap = mapValues { it.value.invert() })

    override fun isTrue(): BitValue = BitListValue(values.map { it.isTrue() }, signed = false).isTrue()

    override infix fun and(other: Value): StructValue {
        require(other is StructValue && other.type == type) { "And on a StructValue can only be performed between another StructValue of the same type!" }
        return copy(valueMap = mapValues { (k,v) -> v and (other[k]!!) })
    }

    override infix fun or(other: Value): StructValue {
        require(other is StructValue && other.type == type) { "Or on a StructValue can only be performed between another StructValue of the same type!" }
        return copy(valueMap = mapValues { (k,v) -> v or (other[k]!!) })
    }

    override infix fun xor(other: Value): StructValue {
        require(other is StructValue && other.type == type) { "Xor on a StructValue can only be performed between another StructValue of the same type!" }
        return copy(valueMap = mapValues { (k,v) -> v xor (other[k]!!) })
    }

    override fun reverse() = error("reverse() can't be called on StructValues!")

    override fun resizeToMatch(newWidth: SignalWidth): StructValue {
        require(newWidth == signalWidth) { "Cannot resize StructValue to match other value!" }
        return this
    }


    fun selectMember(selection: SignalSelector.Struct): Value {
        return valueMap[selection.member] ?: throw SignalSelectionException(
            selection,
            "Member $selection is not part of the struct $type"
        )
    }
}
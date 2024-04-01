package com.alchitry.labs2.parsers.lucidv2.values

import com.alchitry.labs2.parsers.lucidv2.types.SignalSelectionException
import com.alchitry.labs2.parsers.lucidv2.types.SignalSelector
import com.alchitry.labs2.parsers.lucidv2.types.StructType

data class StructValue(
    val type: StructType,
    private val valueMap: Map<String, Value>,
) : Value(), Map<String, Value> by valueMap {
    init {
        check(valueMap.keys.containsAll(type.keys)) { "A StructValue's value must contain all the keys in the StructType!" }
        check(type.keys.containsAll(valueMap.keys)) { "A StructValue's value must contain only the keys in the StructType!" }
    }

    override fun toString(format: ValueFormat): String = valueMap.mapValues { it.value.toString(format) }.toString()

    override val constant: Boolean
        get() = valueMap.values.all { it.constant }

    override fun isNumber(): Boolean = values.all { it.isNumber() }

    override fun asMutable(): StructValue = copy(valueMap = mapValues { it.value.asMutable() })

    /** withSign() does nothing to struct values since the members have explicit signs */
    override fun withSign(signed: Boolean) = this

    override val width: StructWidth = StructWidth(type)

    override fun invert(): StructValue = copy(valueMap = mapValues { it.value.invert() })

    override fun replaceBit(old: Bit, new: Bit) = copy(valueMap = mapValues { it.value.replaceBit(old, new) })

    override fun where(bit: Bit) = copy(valueMap = mapValues { it.value.where(bit) })
    override fun replace(mask: Value, bit: Bit): Value {
        require(mask is StructValue && mask.type == type)
        return copy(valueMap = mapValues { it.value.replace(mask[it.key]!!, bit) })
    }

    override fun isTrue(): BitValue = BitListValue(values.map { it.isTrue() }, signed = false).isTrue()

    override infix fun and(other: Value): StructValue {
        require(other is StructValue && other.type == type) { "And on a StructValue can only be performed between another StructValue of the same type!" }
        return copy(valueMap = mapValues { (k, v) -> v and (other[k]!!) })
    }

    override infix fun or(other: Value): StructValue {
        require(other is StructValue && other.type == type) { "Or on a StructValue can only be performed between another StructValue of the same type!" }
        return copy(valueMap = mapValues { (k, v) -> v or (other[k]!!) })
    }

    override infix fun xor(other: Value): StructValue {
        require(other is StructValue && other.type == type) { "Xor on a StructValue can only be performed between another StructValue of the same type!" }
        return copy(valueMap = mapValues { (k, v) -> v xor (other[k]!!) })
    }

    override fun reverse() = error("reverse() can't be called on StructValues!")

    override fun resizeToMatch(newWidth: SignalWidth): StructValue {
        require(newWidth == width) { "Cannot resize StructValue to match other value!" }
        return this
    }


    fun selectMember(selection: SignalSelector.Struct): Value {
        return valueMap[selection.member] ?: throw SignalSelectionException(
            selection,
            "Member $selection is not part of the struct $type"
        )
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): StructValue {
        if (selection.isEmpty()) {
            require(newValue is StructValue && newValue.width == width) {
                "Attempted to write an incompatible value to a StructValue!"
            }
            return newValue
        }
        when (val selector = selection.first()) {
            is SignalSelector.Bits -> error("Attempted to select bits on a StructValue!")
            is SignalSelector.Struct -> {
                require(type.containsKey(selector.member)) { "Member $selector is not part of the struct $type" }
                return copy(valueMap = valueMap.mapValues { (k, v) ->
                    if (k == selector.member)
                        v.write(selection.subList(1, selection.size), newValue)
                    else
                        v
                })
            }
        }
    }
}
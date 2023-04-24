package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector
import com.alchitry.labs.parsers.lucidv2.signals.StructType
import java.math.BigInteger

sealed class Value {
    abstract val constant: Boolean

    fun isNumber(): Boolean {
        when (this) {
            is ArrayValue -> {
                this.elements.forEach { if (!it.isNumber()) return false }
                return true
            }

            is SimpleValue -> {
                return this.bits.isNumber()
            }

            is StructValue -> {
                this.values.forEach { if (!it.isNumber()) return false }
                return true
            }

            is UndefinedValue -> {
                return false
            }
        }
    }

    /** Makes a copy of this Value that has the constant flag set as false. */
    fun asMutable(): Value =
        when (this) {
            is ArrayValue -> this.copy(elements.map { it.asMutable() })
            is SimpleValue -> this.copy(constant = false)
            is StructValue -> this.copy(valueMap = mapValues { it.value.asMutable() }.toMutableMap())
            is UndefinedValue -> this.copy(constant = false)
        }


    val signalWidth: SignalWidth
        get() {
            return when (this) {
                is SimpleValue -> SimpleWidth(bits.size)
                is ArrayValue -> ArrayWidth(elements.size, elements[0].signalWidth)
                is StructValue -> StructWidth(type)
                is UndefinedValue -> width
            }
        }

    fun invert(): Value {
        return when (this) {
            is SimpleValue -> SimpleValue(bits.invert(), constant)
            is ArrayValue -> ArrayValue((List(elements.size) { elements[it].invert() }))
            is StructValue -> StructValue(type, this.mapValues { (_, v) -> v.invert() }.toMutableMap())
            is UndefinedValue -> this
        }
    }

    private fun isTrueBit(): Bit {
        return when (this) {
            is SimpleValue -> bits.isTrue()
            is ArrayValue -> MutableBitList(false, elements.size) { elements[it].isTrueBit() }.isTrue()
            is StructValue -> {
                if (isComplete())
                    MutableBitList().also { this.forEach { e -> it.add(e.value.isTrueBit()) } }.isTrue()
                else
                    Bit.Bx
            }

            is UndefinedValue -> Bit.Bx
        }
    }

    fun not(): SimpleValue {
        return isTrueBit().not().toSimpleValue(constant)
    }

    fun isTrue(): SimpleValue {
        return isTrueBit().toSimpleValue(constant)
    }

    infix fun and(other: Value): Value {
        check(other::class == this::class) { "And operator can't be used on different value types" }
        return when (this) {
            is SimpleValue -> SimpleValue(bits and (other as SimpleValue).bits, constant)
            is ArrayValue -> ArrayValue(List(elements.size) { elements[it] and (other as ArrayValue).elements[it] })
            is StructValue -> {
                if (isComplete() && (other as StructValue).isComplete()) {
                    StructValue(type, this.mapValues { (k, v) -> v and (other[k] as Value) }.toMutableMap())
                } else {
                    error("Both structs are not complete")
                }
            }

            is UndefinedValue -> this
        }
    }

    infix fun or(other: Value): Value {
        check(other::class == this::class) { "Or operator can't be used on different value types" }
        return when (this) {
            is SimpleValue -> SimpleValue(bits or (other as SimpleValue).bits, constant)
            is ArrayValue -> ArrayValue(List(elements.size) { elements[it] or (other as ArrayValue).elements[it] })
            is StructValue -> {
                if (isComplete() && (other as StructValue).isComplete()) {
                    StructValue(type, this.mapValues { (k, v) -> v or (other[k] as Value) }.toMutableMap())
                } else {
                    error("Both structs are not complete")
                }
            }

            is UndefinedValue -> this
        }
    }

    infix fun xor(other: Value): Value {
        check(other::class == this::class) { "Xor operator can't be used on different value types" }
        return when (this) {
            is SimpleValue -> SimpleValue(bits xor (other as SimpleValue).bits, constant)
            is ArrayValue -> ArrayValue(List(elements.size) { elements[it] xor (other as ArrayValue).elements[it] })
            is StructValue -> {
                if (isComplete() && (other as StructValue).isComplete()) {
                    StructValue(type, this.mapValues { (k, v) -> v xor (other[k] as Value) }.toMutableMap())
                } else {
                    error("Both structs are not complete")
                }
            }

            is UndefinedValue -> this
        }
    }

    infix fun isNotEqualTo(other: Value): SimpleValue {
        return xor(other).orReduce()
    }

    infix fun isEqualTo(other: Value): SimpleValue {
        return isNotEqualTo(other).lsb.not().toSimpleValue(constant && other.constant)
    }

    private fun reduceOp(op: (BitList) -> Bit): Bit {
        return when (this) {
            is SimpleValue -> op(bits)
            is ArrayValue -> op(MutableBitList(false, elements.size) { elements[it].reduceOp(op) })
            is StructValue -> {
                if (isComplete()) {
                    op(MutableBitList(false).also { it.addAll(this.map { (_, v) -> v.reduceOp(op) }) })
                } else {
                    Bit.Bx
                }
            }

            is UndefinedValue -> Bit.Bx
        }
    }

    fun andReduce(): SimpleValue {
        return reduceOp { it.reduce { a, b -> a and b } }.toSimpleValue(constant)
    }

    fun orReduce(): SimpleValue {
        return reduceOp { it.reduce { a, b -> a or b } }.toSimpleValue(constant)
    }

    fun xorReduce(): SimpleValue {
        return reduceOp { it.reduce { a, b -> a xor b } }.toSimpleValue(constant)
    }

    fun reverse(): Value {
        return when (this) {
            is ArrayValue -> ArrayValue(elements.reversed())
            is SimpleValue -> SimpleValue(MutableBitList(signed, bits.reversed()), constant)
            is StructValue -> error("reverse() can't be called on StructValues")
            is UndefinedValue -> if (signalWidth.isArray()) this else error("reverse() can't be called on UndefinedValues that aren't arrays")
        }
    }

    private fun getBits(): BitList {
        return when (this) {
            is ArrayValue -> MutableBitList().also { bits ->
                elements.forEach { bits.addAll(it.getBits()) }
            }

            is SimpleValue -> bits
            is StructValue -> MutableBitList().also { bits ->
                type.forEach { (key, value) ->
                    val elementBits =
                        this[key]?.getBits() ?: MutableBitList(Bit.Bx, value.getBitCount()) as List<Bit>
                    bits.addAll(elementBits)
                }
            }

            is UndefinedValue -> MutableBitList(Bit.Bx, width.getBitCount())
        }
    }

    fun flatten(): SimpleValue = SimpleValue(getBits(), constant)

    /** Returns true if the other value can be scaled to match this value. */
    fun canAssign(other: Value): Boolean = signalWidth.canAssign(other.signalWidth)

    /** Resizes this Value to be the same width as the other value.
     *  It may truncate values if other is smaller than this value.
     */
    fun resizeToMatch(other: Value): Value =
        when (this) {
            is ArrayValue -> {
                if (signalWidth != other.signalWidth)
                    error("Cannot resize ArrayValue to match other value")
                this
            }
            is SimpleValue -> when (other) {
                is ArrayValue -> error("Cannot resize SimpleValue to fit an ArrayValue")
                is SimpleValue -> resize(other.size)
                is StructValue -> error("Cannot resize SimpleValue to fit a StructValue")
                is UndefinedValue -> UndefinedValue(constant = constant, width = other.width)
            }
            is StructValue -> {
                if (signalWidth != other.signalWidth)
                    error("Cannot resize StructValue to match other value")
                this
            }
            is UndefinedValue -> copy(width = other.signalWidth)
        }

    fun select(selection: SignalSelector): Value =
        when (selection) {
            is SignalSelector.Bits -> when (this) {
                is ArrayValue -> select(selection)
                is SimpleValue -> select(selection)
                is StructValue -> throw SignalSelectionException(selection, "Bit selection can't be used on a struct")
                is UndefinedValue -> TODO()
            }
            is SignalSelector.Struct -> when (this) {
                is ArrayValue, is SimpleValue -> throw SignalSelectionException(selection, "Arrays don't have struct members")
                is StructValue -> select(selection)
                is UndefinedValue -> TODO()
            }
        }
}

data class ArrayValue(
    val elements: List<Value>
) : Value(), List<Value> by elements {
    override val constant: Boolean
        get() = all { it.constant }

    fun select(selection: SignalSelector.Bits): Value {
        try {
            val newElements = elements.subList(selection.range.first, selection.range.last + 1)
            if (newElements.size == 1)
                return newElements.first()
            return copy(elements = newElements)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(selection, "Selection $selection is outside the bounds of [${elements.size-1}:0]")
        }
    }
}

fun BigInteger.toValue(constant: Boolean): SimpleValue = SimpleValue(MutableBitList(this), constant)

data class SimpleValue(
    val bits: BitList,
    override val constant: Boolean
) : Value(), List<Bit> by bits {
    val signed: Boolean
        get() = bits.signed

    fun toBigInt(): BigInteger = bits.toBigInt()

    fun resize(width: Int) = bits.resize(width).toSimpleValue(constant)

    /** Changes sign without changing bits */
    fun setSign(signed: Boolean): SimpleValue {
        return SimpleValue(MutableBitList(signed, bits), constant)
    }

    infix fun isLessThan(other: SimpleValue): SimpleValue {
        val longest = size.coerceAtLeast(other.size)
        val signedOp = signed && other.signed
        val op1 = setSign(signedOp).resize(longest)
        val op2 = other.setSign(signedOp).resize(longest)

        val neg1 = signedOp && bits.isNegative()
        val neg2 = signedOp && other.bits.isNegative()

        if (neg1 && !neg2) // negative < positive
            return Bit.B1.toSimpleValue(constant && other.constant)

        if (!neg1 && neg2) // positive !< negative
            return Bit.B0.toSimpleValue(constant && other.constant)

        // negative to negative or positive to positive can be directly compared
        for (i in op1.indices.reversed()) {
            if (!op1[i].isNumber() || !op2[i].isNumber())
                return Bit.Bx.toSimpleValue(constant && other.constant)

            if (op1[i] == Bit.B1 && op2[i] == Bit.B0)
                return Bit.B0.toSimpleValue(constant && other.constant)

            if (op1[i] == Bit.B0 && op2[i] == Bit.B1)
                return Bit.B1.toSimpleValue(constant && other.constant)
        }
        return Bit.B0.toSimpleValue(constant && other.constant)
    }

    infix fun isGreaterThan(other: SimpleValue): SimpleValue {
        return other.isLessThan(this).lsb.toSimpleValue(constant && other.constant)
    }

    infix fun isLessOrEqualTo(other: SimpleValue): SimpleValue {
        return (isLessThan(other).lsb or isEqualTo(other).lsb).toSimpleValue(constant && other.constant)
    }

    infix fun isGreaterOrEqualTo(other: SimpleValue): SimpleValue {
        return (isGreaterThan(other).lsb or isEqualTo(other).lsb).toSimpleValue(constant && other.constant)
    }

    fun toBoolean() = bits.isTrue() == Bit.B1

    fun select(selection: SignalSelector.Bits): Value {
        try {
            val newBits = bits.subList(selection.range.first, selection.range.last + 1)
            return copy(bits = newBits)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(selection, "Selection $selection is outside the bounds of [${bits.size-1}:0]")
        }
    }
}

data class StructValue(
    val type: StructType,
    private val valueMap: MutableMap<String, Value> = mutableMapOf()
) : Value(), Map<String, Value> by valueMap {
    init {
        valueMap.keys.removeIf { !type.contains(it) }
        type.keys.forEach {
            valueMap.putIfAbsent(it, UndefinedValue(constant))
        }
    }

    override val constant: Boolean
        get() = valueMap.values.all { it.constant }

    fun isComplete(): Boolean {
        return type.keys.all { this[it] !is UndefinedValue }
    }

    fun select(selection: SignalSelector.Struct): Value {
        return valueMap[selection.member] ?: throw SignalSelectionException(selection, "Member $selection is not part of the struct $type")
    }
}

data class UndefinedValue(
    override val constant: Boolean,
    val width: SignalWidth = UndefinedSimpleWidth
) : Value()

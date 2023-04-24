package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

sealed class Value {
    abstract val constant: Boolean
    abstract fun isNumber(): Boolean

//    fun isNumber(): Boolean {
//        when (this) {
//            is ArrayValue -> {
//                this.elements.forEach { if (!it.isNumber()) return false }
//                return true
//            }
//
//            is SimpleValue -> {
//                return this.bits.isNumber()
//            }
//
//            is StructValue -> {
//                this.values.forEach { if (!it.isNumber()) return false }
//                return true
//            }
//
//            is UndefinedValue -> {
//                return false
//            }
//        }
//    }


    /** Makes a copy of this Value that has the constant flag set as false. */
    abstract fun asMutable(): Value
//        when (this) {
//            is ArrayValue -> this.copy(elements.map { it.asMutable() })
//            is SimpleValue -> this.copy(constant = false)
//            is StructValue -> this.copy(valueMap = mapValues { it.value.asMutable() }.toMutableMap())
//            is UndefinedValue -> this.copy(constant = false)
//        }


    abstract val signalWidth: SignalWidth
//        get() {
//            return when (this) {
//                is SimpleValue -> SimpleWidth(bits.size)
//                is ArrayValue -> ArrayWidth(elements.size, elements[0].signalWidth)
//                is StructValue -> StructWidth(type)
//                is UndefinedValue -> width
//            }
//        }

    abstract fun invert(): Value
//        return when (this) {
//            is SimpleValue -> SimpleValue(bits.invert(), constant)
//            is ArrayValue -> ArrayValue((List(elements.size) { elements[it].invert() }))
//            is StructValue -> StructValue(type, this.mapValues { (_, v) -> v.invert() }.toMutableMap())
//            is UndefinedValue -> this
//        }


//    private fun isTrueBit(): Bit {
//        return when (this) {
//            is SimpleValue -> bits.isTrue()
//            is ArrayValue -> MutableBitList(false, elements.size) { elements[it].isTrueBit() }.isTrue()
//            is StructValue -> {
//                if (isComplete())
//                    MutableBitList().also { this.forEach { e -> it.add(e.value.isTrueBit()) } }.isTrue()
//                else
//                    Bit.Bx
//            }
//
//            is UndefinedValue -> Bit.Bx
//        }
//    }

    abstract fun isTrue(): BitValue

    infix fun and(other: Value): Value {
        check(other::class == this::class) { "And operator can't be used on different value types" }
        return when (this) {
            is BitValue -> this and (other as BitValue)
            is SimpleValue -> this and (other as SimpleValue)
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
            is BitValue -> this or (other as BitValue)
            is SimpleValue -> this or (other as SimpleValue)
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
            is BitValue -> this xor (other as BitValue)
            is SimpleValue -> this xor (other as SimpleValue)
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

    infix fun isNotEqualTo(other: Value): BitValue {
        return xor(other).orReduce()
    }

    infix fun isEqualTo(other: Value): BitValue {
        return isNotEqualTo(other).invert()
    }

    private fun reduceOp(op: (List<Bit>) -> Bit): Bit {
        return when (this) {
            is BitValue -> op(listOf(bit))
            is SimpleValue -> op(bits)
            is ArrayValue -> op(elements.map { it.reduceOp(op) })
            is StructValue -> {
                if (isComplete()) {
                    op(this.map { (_, v) -> v.reduceOp(op) })
                } else {
                    Bit.Bx
                }
            }

            is UndefinedValue -> Bit.Bx
        }
    }

    fun andReduce(): BitValue {
        return reduceOp { it.reduce { a, b -> a and b } }.toBitValue(constant)
    }

    fun orReduce(): BitValue {
        return reduceOp { it.reduce { a, b -> a or b } }.toBitValue(constant)
    }

    fun xorReduce(): BitValue {
        return reduceOp { it.reduce { a, b -> a xor b } }.toBitValue(constant)
    }

    fun reverse(): Value {
        return when (this) {
            is BitValue -> this
            is ArrayValue -> ArrayValue(elements.reversed())
            is SimpleValue -> copy(bits.reversed())
            is StructValue -> error("reverse() can't be called on StructValues")
            is UndefinedValue -> if (signalWidth.isArray()) this else error("reverse() can't be called on UndefinedValues that aren't arrays")
        }
    }

    private fun getBits(): List<Bit> {
        return when (this) {
            is BitValue -> listOf(bit)
            is ArrayValue -> elements.flatMap { it.getBits() }
            is SimpleValue -> bits
            is StructValue -> type.flatMap { (key, value) ->
                this[key]?.getBits() ?: List(value.getBitCount()) { Bit.Bx }
            }
            is UndefinedValue -> List(width.getBitCount()) { Bit.Bx }
        }
    }

    fun flatten(): SimpleValue = SimpleValue(getBits(), constant, false)

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

            is BitValue -> when (other) {
                is BitValue -> this
                is ArrayValue -> error("Cannot resize SimpleValue to fit an ArrayValue")
                is SimpleValue -> SimpleValue(listOf(bit), constant, signed).resize(other.size)
                is StructValue -> error("Cannot resize SimpleValue to fit a StructValue")
                is UndefinedValue -> UndefinedValue(constant = constant, width = other.width)
            }

            is SimpleValue -> when (other) {
                is ArrayValue -> error("Cannot resize SimpleValue to fit an ArrayValue")
                is SimpleValue -> resize(other.size)
                is BitValue -> BitValue(lsb, constant, signed)
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
                is BitValue -> TODO()
            }

            is SignalSelector.Struct -> when (this) {
                is ArrayValue, is SimpleValue -> throw SignalSelectionException(
                    selection,
                    "Arrays don't have struct members"
                )

                is StructValue -> select(selection)
                is UndefinedValue -> TODO()
                is BitValue -> TODO()
            }
        }
}







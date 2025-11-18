package com.alchitry.labs2.parsers.hdl.values

import com.alchitry.labs2.parsers.hdl.types.SignalSelectionException
import com.alchitry.labs2.parsers.hdl.types.SignalSelector

/**
 * Used to represent values that WILL be defined but currently aren't.
 * This should only happen from parameters without a default or test value and any values derived from them.
 */
data class UndefinedValue(
    override val width: SignalWidth
) : Value() {
    override fun isNumber() = false

    override fun withSign(signed: Boolean) = this

    override fun invert() = this
    override fun replaceBit(old: Bit, new: Bit) = this

    override fun isTrue() = BitValue(Bit.Bx, false)

    override fun where(bit: Bit) = this.width.filledWith(Bit.B0, false)
    override fun replace(mask: Value, bit: Bit) = this

    override fun toString(format: ValueFormat): String = "UndefinedValue($width)"

    fun selectMember(selection: SignalSelector.Struct): UndefinedValue {
        return when (width) {
            is StructWidth -> {
                UndefinedValue(
                    width.type[selection.member]?.width
                        ?: throw SignalSelectionException(
                            selection,
                            "Struct type \"${width.type.name}\" doesn't have a member named \"${selection.member}\"!"
                        )
                )
            }

            else -> throw SignalSelectionException(selection, "Struct selector used on ${width::class.simpleName}!")
        }
    }

    fun selectBits(selection: SignalSelector.Bits): UndefinedValue {
        return when (width) {
            is BitWidth -> throw SignalSelectionException(selection, "BitWidth can't be selected!")
            is SimpleWidth -> {
                val count = if (width is DefinedSimpleWidth) {
                    if (!(selection.range.first <= width.size && selection.range.last <= width.size)) {
                        throw SignalSelectionException(
                            selection,
                            "Selection of ${selection.range} is out of range of the value's width of ${width.size}!"
                        )
                    }
                    selection.count(width.size)
                } else {
                    selection.count()
                }
                when (count) {
                    1 -> UndefinedValue(BitWidth)
                    null -> UndefinedValue(UndefinedSimpleWidth())
                    else -> UndefinedValue(DefinedSimpleWidth(count))
                }
            }

            is StructWidth -> throw SignalSelectionException(selection, "Bit selector used on StructWidth!")

            is ArrayWidth -> {
                val count = if (width is DefinedArrayWidth) {
                    if (!(selection.range.first <= width.size && selection.range.last <= width.size)) {
                        throw SignalSelectionException(
                            selection,
                            "Selection of ${selection.range} is out of range of the value's width of ${width.size}!"
                        )
                    }
                    selection.count(width.size)
                } else {
                    selection.count()
                }
                when (count) {
                    1 -> UndefinedValue(width.next)
                    null -> UndefinedValue(UndefinedArrayWidth(width.next))
                    else -> UndefinedValue(DefinedArrayWidth(count, width.next))
                }
            }

        }
    }

    override infix fun and(other: Value): UndefinedValue {
        require(other is SimpleValue || other is UndefinedValue) { "And can only be performed with Undefined or Simple values." }
        return UndefinedValue(width)
    }

    override fun or(other: Value): UndefinedValue {
        require(other is SimpleValue || other is UndefinedValue) { "Or can only be performed with Undefined or Simple values." }
        return UndefinedValue(width)
    }

    override fun xor(other: Value): UndefinedValue {
        require(other is SimpleValue || other is UndefinedValue) { "Xor can only be performed with Undefined or Simple values." }
        return UndefinedValue(width)
    }

    override fun reverse(): UndefinedValue =
        if (this.width.isArrayOrSimple()) this else error("reverse() can't be called on UndefinedValues that aren't arrays.")

    override fun resizeToMatch(newWidth: SignalWidth): UndefinedValue {
        check(this.width.canAssign(newWidth)) { "UndefinedValue width ${this.width} could not be resized to new width of $newWidth!" }
        return copy(width = newWidth)
    }

    override fun write(selection: List<SignalSelector>, newValue: Value): Value {
        return if (selection.isEmpty()) {
            require(width.isCompatibleWith(newValue.width)) { "Attempted to write an incompatible type to the UndefinedValue!" }
            newValue
        } else {
            this
        }
    }
}

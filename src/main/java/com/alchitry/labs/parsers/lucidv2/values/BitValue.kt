package com.alchitry.labs.parsers.lucidv2.values

enum class BitValue {
    B0,
    B1,
    Bx, // used to specify unknown values (reads are ok)
    Bz, // used to specify high-impedance (reads are errors, should only be used at top-level output)
    Bu; // used to specify un-driven signals (reads are errors)

    infix fun or(b: BitValue): BitValue {
        if (this == B1 || b == B1) return B1
        if (!isNumber() || !b.isNumber()) return Bx
        return B0
    }

    infix fun and(b: BitValue): BitValue {
        if (this == B1 && b == B1) return B1
        if (!isNumber() || !b.isNumber()) return Bx
        return B0
    }

    infix fun xor(b: BitValue): BitValue {
        if (!isNumber() || !b.isNumber()) return Bx
        if (this == B1 != (b == B1)) return B1
        return B0
    }

    val char: Char
        get() = when (this) {
            B0 -> '0'
            B1 -> '1'
            Bx -> 'x'
            Bz -> 'z'
            Bu -> 'u'
        }

    infix fun nor(b: BitValue): BitValue {
        return or(b).not()
    }

    infix fun nand(b: BitValue): BitValue {
        return and(b).not()
    }

    infix fun xnor(b: BitValue): BitValue {
        return xor(b).not()
    }

    operator fun not(): BitValue {
        return invert()
    }

    fun invert(): BitValue {
        return when (this) {
            B0 -> B1
            B1 -> B0
            Bx -> Bx
            Bz -> Bx
            Bu -> Bx
        }
    }

    fun isNumber(): Boolean {
        return when (this) {
            B0, B1 -> true
            Bx, Bz, Bu -> false
        }
    }

    fun toBitArray(): BitList = MutableBitList(this)
    fun toSimpleValue(): SimpleValue = SimpleValue(toBitArray())
}
package com.alchitry.labs.parsers.lucidv2.values

enum class Bit {
    B0,
    B1,
    Bx, // used to specify unknown values (reads are ok)
    Bz; // used to specify high-impedance (reads are errors, should only be used at top-level output)

    infix fun or(b: Bit): Bit {
        if (this == B1 || b == B1) return B1
        if (this == B0 && b == B0) return B0
        return Bx
    }

    infix fun and(b: Bit): Bit {
        if (this == B1 && b == B1) return B1
        if (this == B0 || b == B0) return B0
        return Bx
    }

    infix fun xor(b: Bit): Bit {
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
        }

    infix fun nor(b: Bit): Bit {
        return or(b).not()
    }

    infix fun nand(b: Bit): Bit {
        return and(b).not()
    }

    infix fun xnor(b: Bit): Bit {
        return xor(b).not()
    }

    operator fun not(): Bit {
        return invert()
    }

    fun invert(): Bit {
        return when (this) {
            B0 -> B1
            B1 -> B0
            Bx -> Bx
            Bz -> Bx
        }
    }

    fun isNumber(): Boolean {
        return when (this) {
            B0, B1 -> true
            Bx, Bz -> false
        }
    }

    fun toBitValue(constant: Boolean, signed: Boolean = false): BitValue = BitValue(this, constant, signed)
}
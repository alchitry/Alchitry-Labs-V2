package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.hdl.lucidv2.values.SignalWidth

data class StructType(
    val name: String,
    private val members: LinkedHashMap<String, StructMember> // linked required to keep the order of members
) : Map<String, StructMember> by members {
    fun offsetOf(member: String): Int? {
        val keys = members.keys
        val offset = keys.indexOf(member)
        if (offset < 0)
            error("The member \"$member\" is not a valid key.")
        return (0..<offset).sumOf { members[keys.elementAt(it)]!!.width.bitCount ?: return null }
    }
}

data class StructMember(val name: String, val width: SignalWidth, val signed: Boolean)
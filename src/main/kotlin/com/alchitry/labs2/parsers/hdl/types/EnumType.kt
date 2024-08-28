package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.BitUtil
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitListWidth

data class EnumType(
    override val name: String,
    val members: Set<String>,
    override val parent: SignalParent?
) : SignalParent, Measurable {
    init {
        require(members.isNotEmpty()) { "EnumType members must contain at least one member!" }
    }

    override val width = BitListWidth(BitUtil.minWidthNum(members.size - 1))
    val memberSignals = members.mapIndexed { idx, name ->
        BitListValue(idx, width.size, signed = false).asSignal(
            name,
            ExprType.Constant,
            this
        )
    }.associateBy { it.name }

    override fun getSignal(name: String) = memberSignals[name]
}
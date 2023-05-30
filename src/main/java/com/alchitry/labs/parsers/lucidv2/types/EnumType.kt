package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.Util
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitListWidth

data class EnumType(
    override val name: String,
    val members: Set<String>,
    override val parent: SignalParent?
) : SignalParent, Measurable {
    init {
        require(members.isNotEmpty()) { "EnumType members must contain at least one member!" }
        require(!members.contains("WIDTH")) { "EnumType can't have a member named WIDTH!" }
    }

    override val width = BitListWidth(Util.minWidthNum(members.size - 1))
    private val widthSignal = BitListValue(width.size, constant = true, signed = false).asSignal("WIDTH", this)
    private val memberSignals = members.mapIndexed { idx, name ->
        BitListValue(idx, width.size, constant = true, signed = false).asSignal(
            name,
            this
        )
    }.associateBy { it.name }

    override fun getSignal(name: String) = if (name == "WIDTH") widthSignal else memberSignals[name]
}
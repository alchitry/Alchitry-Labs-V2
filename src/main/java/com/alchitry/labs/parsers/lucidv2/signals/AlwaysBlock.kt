package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.AlwaysBlockContext

class AlwaysBlock(
    private val context: LucidModuleContext,
    val inputs: List<Signal>,
    val outputs: List<Signal>,
    val alwaysBlockContext: AlwaysBlockContext
) {
}
package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener


/**
 * This is responsible for checking that all the bits of signals that are expected to be driven are driven.
 */
data class SignalDriverParser(
    private val context: LucidModuleContext
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)


}
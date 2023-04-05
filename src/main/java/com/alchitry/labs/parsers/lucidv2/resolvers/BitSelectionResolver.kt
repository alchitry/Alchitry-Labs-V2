package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.Bit_selectionContext

interface BitSelectionResolver {
    fun resolve(ctx: Bit_selectionContext): List<IntRange>
}
package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.BitSelectionContext

interface BitSelectionResolver {
    fun resolve(ctx: BitSelectionContext): List<IntRange>
}
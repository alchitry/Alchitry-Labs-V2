package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.StructTypeContext
import com.alchitry.labs.parsers.lucidv2.signals.StructType

interface StructResolver {
    fun resolve(ctx: StructTypeContext): StructType
}
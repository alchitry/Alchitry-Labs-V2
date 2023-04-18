package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.StructTypeContext

interface StructResolver {
    fun resolve(ctx: StructTypeContext): StructType
}
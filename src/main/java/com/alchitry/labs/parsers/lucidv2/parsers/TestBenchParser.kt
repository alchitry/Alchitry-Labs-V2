package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.TestBenchContext
import com.alchitry.labs.parsers.lucidv2.types.TestBench

class TestBenchParser(
    private val context: LucidTestBenchContext
) : LucidBaseListener() {
    override fun exitTestBench(ctx: TestBenchContext) {
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null) {
            context.reportError(ctx.name(), "Test bench names must start with a lowercase letter.")
            return
        }

        context.project.addTestBench(TestBench(name, context.project, ctx))
    }
}
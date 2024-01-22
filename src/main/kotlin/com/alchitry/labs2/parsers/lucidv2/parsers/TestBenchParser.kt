package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser.TestBenchContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs2.parsers.lucidv2.types.TestBench

class TestBenchParser(
    private val context: LucidTestBenchContext
) : LucidBaseListener() {
    override fun exitTestBench(ctx: TestBenchContext) {
        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text

        if (nameCtx.TYPE_ID() == null) {
            context.reportError(nameCtx, "Test bench names must start with a lowercase letter.")
            return
        }

        if (!context.project.addTestBench(
            TestBench(
                name,
                context.sourceFile,
                context.project,
                ctx
            )
            )
        ) {
            context.reportError(nameCtx, "Test bench name \"$name\" is already in use.")
        }
    }
}
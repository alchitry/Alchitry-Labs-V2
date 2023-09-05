package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.parsers.ParseTreeMultiWalker
import com.alchitry.labs.parsers.lucidv2.parsers.TestBenchParser
import com.alchitry.labs.parsers.lucidv2.parsers.WalkerFilter

class LucidTestBenchContext(
    val project: ProjectContext,
    val errorCollector: ErrorCollector
) : ErrorListener by errorCollector {

    val testBench = TestBenchParser(this)

    suspend fun walk(t: SourceContext) = ParseTreeMultiWalker.walk(
        listOf(testBench),
        t,
        WalkerFilter.join(WalkerFilter.TestBenchesOnly, WalkerFilter.SkipModuleBodies)
    )
}
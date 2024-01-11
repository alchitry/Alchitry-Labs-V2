package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.parsers.ParseTreeMultiWalker
import com.alchitry.labs.parsers.lucidv2.parsers.TestBenchParser
import com.alchitry.labs.parsers.lucidv2.parsers.WalkerFilter
import com.alchitry.labs.parsers.notations.ErrorListener
import com.alchitry.labs.project.files.SourceFile

class LucidTestBenchContext(
    val project: ProjectContext,
    val sourceFile: SourceFile
) : ErrorListener by project.notationManager.getCollector(sourceFile) {
    val notationCollector = project.notationManager.getCollector(sourceFile)

    val testBench = TestBenchParser(this)

    suspend fun walk(t: SourceContext) = ParseTreeMultiWalker.walk(
        listOf(testBench),
        t,
        WalkerFilter.join(WalkerFilter.TestBenchesOnly, WalkerFilter.SkipModuleBodies)
    )
}
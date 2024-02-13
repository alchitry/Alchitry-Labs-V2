package com.alchitry.labs2.parsers.lucidv2.context

import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs2.parsers.lucidv2.parsers.TestBenchParser
import com.alchitry.labs2.parsers.lucidv2.parsers.WalkerFilter
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.project.files.SourceFile

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
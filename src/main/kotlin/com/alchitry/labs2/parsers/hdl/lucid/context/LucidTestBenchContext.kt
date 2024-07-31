package com.alchitry.labs2.parsers.hdl.lucid.context

import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.lucid.parsers.TestBenchParser
import com.alchitry.labs2.parsers.hdl.lucid.parsers.WalkerFilter
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext

class LucidTestBenchContext(
    val project: ProjectContext,
    val sourceFile: SourceFile
) : ErrorListener by project.notationManager.getCollector(sourceFile) {
    val notationCollector = project.notationManager.getCollector(sourceFile)

    val testBench = TestBenchParser(this)

    fun walk(t: ParserRuleContext) = ParseTreeMultiWalker.walk(
        listOf(testBench),
        t,
        WalkerFilter.join(WalkerFilter.TestBenchesOnly, WalkerFilter.SkipModuleBodies)
    )
}
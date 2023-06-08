package com.alchitry.labs.ui.code_editor.styles.lucid

import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.ui.code_editor.StyleToken
import com.alchitry.labs.ui.code_editor.styles.CodeErrorChecker
import com.alchitry.labs.ui.code_editor.toStyleToken
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class LucidErrorChecker : CodeErrorChecker {
    override fun checkText(text: String): List<StyleToken> {
        val project = ProjectContext()
        val errorCollector = ErrorCollector()

        val parser = LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(text)
                ).also { it.removeErrorListeners() })
        ).apply {
            (tokenStream.tokenSource as LucidLexer).addErrorListener(errorCollector)
            removeErrorListeners()
            addErrorListener(errorCollector)
        }

        val tree = parser.source()

        if (errorCollector.hasSyntaxIssues) {
            return errorCollector.getAllNotations().map { it.toStyleToken() }
        }

        val moduleTypeContext = LucidModuleTypeContext(project, errorCollector)
        val module = moduleTypeContext.extract(tree)

        if (errorCollector.hasErrors) {
            return errorCollector.getAllNotations().map { it.toStyleToken() }
        }

        if (module != null) {
            val moduleInstance = ModuleInstance("top", project, null, module, mapOf(), mapOf(), errorCollector)

            moduleInstance.initialWalk()

            if (errorCollector.hasErrors) {
                return errorCollector.getAllNotations().map { it.toStyleToken() }
            }
        }

        val testBenchContext = LucidTestBenchContext(project, errorCollector)
        testBenchContext.walk(tree)

        if (errorCollector.hasErrors) {
            return errorCollector.getAllNotations().map { it.toStyleToken() }
        }

        return project.getTestBenches().flatMap { testBench ->
            testBench.initialWalk()
            testBench.context.errorCollector.getAllNotations().map { it.toStyleToken() }
        }
    }
}
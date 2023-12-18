package com.alchitry.labs.ui.code_editor.styles.lucid

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.Notation
import com.alchitry.labs.parsers.errors.NotationCollector
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.project.files.SourceFile
import com.alchitry.labs.ui.code_editor.styles.CodeErrorChecker
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

class LucidErrorChecker : CodeErrorChecker {
    override suspend fun checkText(text: String): List<Notation> {
        val notationCollector = NotationCollector(SourceFile(FileProvider.DiskFile("alchitry_top.luc"), true))

        val parser = LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(text)
                ).also { it.removeErrorListeners() })
        ).apply {
            (tokenStream?.tokenSource as? LucidLexer)?.addErrorListener(notationCollector)
                ?: error("TokenSource was not a LucidLexer!")
            removeErrorListeners()
            addErrorListener(notationCollector)
        }

        val tree = parser.source()

        if (notationCollector.hasErrors) {
            return notationCollector.getAllNotations()
        }

        ProjectContext().use { project ->
            val moduleTypeContext = LucidModuleTypeContext(project, notationCollector)
            val module = moduleTypeContext.extract(tree)

            if (notationCollector.hasErrors) {
                return notationCollector.getAllNotations()
            }

            if (module != null) {
                val moduleInstance =
                    ModuleInstance("top", project, null, module, mapOf(), mapOf(), notationCollector)

                moduleInstance.initialWalk()

                if (notationCollector.hasErrors) {
                    return notationCollector.getAllNotations()
                }
            }

            val testBenchContext = LucidTestBenchContext(project, notationCollector)
            testBenchContext.walk(tree)

            if (notationCollector.hasErrors) {
                return notationCollector.getAllNotations()
            }

            return project.getTestBenches().flatMap { testBench ->
                testBench.initialWalk()
                testBench.context.notationCollector.getAllNotations()
            }
        }
    }
}
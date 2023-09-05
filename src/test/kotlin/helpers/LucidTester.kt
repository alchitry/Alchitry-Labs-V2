package helpers

import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.Project
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import java.io.File

class LucidTester(vararg val files: String) {
    val project = Project("Testing", File("."), Board.AlchitryAu, emptySet(), emptySet(), emptySet())

    fun parseText(errorCollector: ErrorCollector): List<SourceContext> {
        return files.map {
            val parser = LucidParser(
                CommonTokenStream(
                    LucidLexer(
                        CharStreams.fromString(it)
                    ).also { it.removeErrorListeners() })
            ).apply {
                (tokenStream?.tokenSource as LucidLexer).addErrorListener(errorCollector)
                removeErrorListeners()
                addErrorListener(errorCollector)
            }

            parser.source()
        }
    }

    suspend fun globalParse(
        errorCollector: ErrorCollector = testErrorCollector(),
        trees: List<SourceContext> = parseText(errorCollector)
    ) {
        assert(errorCollector.hasNoIssues) { errorCollector.printReport() }

        trees.forEach {
            val globalContext = LucidGlobalContext(project, errorCollector)
            globalContext.walk(it)

            errorCollector.assertNoIssues()
        }
    }

    private suspend fun testBenchParse(
        errorCollector: ErrorCollector = testErrorCollector(),
        trees: List<SourceContext> = parseText(errorCollector)
    ) {
        assert(errorCollector.hasNoIssues) { errorCollector.printReport() }

        trees.forEach {
            val testBenchContext = LucidTestBenchContext(project, errorCollector)
            testBenchContext.walk(it)

            errorCollector.assertNoIssues()
        }
    }

    suspend fun moduleTypeParse(
        errorCollector: ErrorCollector = testErrorCollector(),
        trees: List<SourceContext> = parseText(errorCollector)
    ): List<Module> {
        assert(errorCollector.hasNoIssues) { errorCollector.printReport() }

        return trees.mapNotNull {
            val moduleTypeContext = LucidModuleTypeContext(project, errorCollector)
            val module = moduleTypeContext.extract(it)

            errorCollector.assertNoIssues()
            module
        }
    }

    /**
     * Performs a full parse on the file.
     * @param errorCollector if null, the function will automatically check for errors. If provided, you should check
     * for errors after calling this function.
     */
    suspend fun fullParse(errorCollector: ErrorCollector? = null): ModuleInstance {
        val errors = errorCollector ?: testErrorCollector()
        val trees = parseText(errors)

        globalParse(errors, trees)
        val modules = moduleTypeParse(errors, trees)

        val moduleInstance = ModuleInstance("top", project, null, modules.first(), mapOf(), mapOf(), errors)

        moduleInstance.initialWalk()
        if (errorCollector == null)
            errors.assertNoIssues()

        return moduleInstance
    }

    suspend fun runFirstTestBench(errorCollector: ErrorCollector? = null): SimParent {
        val errors = errorCollector ?: testErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        moduleTypeParse(errors, tree)
        testBenchParse(errors, tree)

        val testBenches = project.getTestBenches()

        val testBench = testBenches.first()
        testBench.initialWalk()
        val tests = testBench.getTestBlocks()

        val test = tests.first()
        return testBench.runTest(test.name).also {
            test.context.errorCollector.assertNoIssues()
        }
    }

    suspend fun runTestBenches(errorCollector: ErrorCollector? = null) {
        val errors = errorCollector ?: testErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        moduleTypeParse(errors, tree)
        testBenchParse(errors, tree)

        val testBenches = project.getTestBenches()

        testBenches.forEach { testBench ->
            testBench.initialWalk()
        }

        errors.assertNoErrors()

        testBenches.forEach { testBench ->
            val tests = testBench.getTestBlocks()
            tests.forEach {
                testBench.runTest(it.name)
                it.context.errorCollector.assertNoIssues()
            }
        }
    }
}
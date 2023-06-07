package helpers

import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import kotlinx.coroutines.runBlocking
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class LucidTester(vararg val files: String) {
    val project = ProjectContext()

    fun parseText(errorCollector: ErrorCollector): List<SourceContext> {
        return files.map {
            val parser = LucidParser(
                CommonTokenStream(
                    LucidLexer(
                        CharStreams.fromString(it)
                    ).also { it.removeErrorListeners() })
            ).apply {
                (tokenStream.tokenSource as LucidLexer).addErrorListener(errorCollector)
                removeErrorListeners()
                addErrorListener(errorCollector)
            }

            parser.source()
        }
    }

    fun globalParse(
        errorCollector: ErrorCollector = ErrorCollector(),
        tree: List<SourceContext> = parseText(errorCollector)
    ) {
        assert(errorCollector.hasNoIssues)

        tree.forEach {
            val globalContext = LucidGlobalContext(project, errorCollector)
            globalContext.walk(it)

            assert(errorCollector.hasNoIssues)
        }
    }

    private fun testBenchParse(
        errorCollector: ErrorCollector = ErrorCollector(),
        tree: List<SourceContext> = parseText(errorCollector)
    ) {
        assert(errorCollector.hasNoIssues)

        tree.forEach {
            val testBenchContext = LucidTestBenchContext(project, errorCollector)
            testBenchContext.walk(it)

            assert(errorCollector.hasNoIssues)
        }
    }

    fun moduleTypeParse(
        errorCollector: ErrorCollector = ErrorCollector(),
        tree: List<SourceContext> = parseText(errorCollector)
    ): List<Module> {
        assert(errorCollector.hasNoIssues)

        return tree.mapNotNull {
            val moduleTypeContext = LucidModuleTypeContext(project, errorCollector)
            val module = moduleTypeContext.extract(it)

            assert(errorCollector.hasNoIssues)
            module
        }
    }

    /**
     * Performs a full parse on the file.
     * @param errorCollector if null, the function will automatically check for errors. If provided, you should check
     * for errors after calling this function.
     */
    fun fullParse(errorCollector: ErrorCollector? = null): ModuleInstance {
        val errors = errorCollector ?: ErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        val modules = moduleTypeParse(errors, tree)

        val moduleInstance = ModuleInstance("top", project, null, modules.first(), mapOf(), mapOf(), errors)

        moduleInstance.initialWalk()
        if (errorCollector == null) {
            assert(errors.hasNoIssues)
        }

        return moduleInstance
    }

    fun runFirstTestBench(errorCollector: ErrorCollector? = null): SimParent {
        val errors = errorCollector ?: ErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        moduleTypeParse(errors, tree)
        testBenchParse(errors, tree)

        val testBenches = project.getTestBenches()

        val testBench = testBenches.first()
        testBench.initialWalk()
        val tests = testBench.getTestBlocks()
        return runBlocking {
            val test = tests.first()
            testBench.runTest(test.name).also {
                assert(test.context.errorCollector.hasNoIssues)
            }
        }
    }

    fun runTestBenches(errorCollector: ErrorCollector? = null) {
        val errors = errorCollector ?: ErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        moduleTypeParse(errors, tree)
        testBenchParse(errors, tree)

        val testBenches = project.getTestBenches()

        testBenches.forEach { testBench ->
            testBench.initialWalk()
            val tests = testBench.getTestBlocks()
            runBlocking {
                tests.forEach {
                    testBench.runTest(it.name)
                    assert(it.context.errorCollector.hasNoIssues)
                }
            }
        }
    }
}
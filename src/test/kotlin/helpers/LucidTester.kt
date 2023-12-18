package helpers

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.NotationCollector
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstanceArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

class LucidTester(vararg val files: String) {
    val project = ProjectContext()

    fun parseText(notationCollector: NotationCollector): List<SourceContext> {
        return files.map {
            val parser = LucidParser(
                CommonTokenStream(
                    LucidLexer(
                        CharStreams.fromString(it)
                    ).also { it.removeErrorListeners() })
            ).apply {
                (tokenStream?.tokenSource as LucidLexer).addErrorListener(notationCollector)
                removeErrorListeners()
                addErrorListener(notationCollector)
            }

            parser.source()
        }
    }

    suspend fun globalParse(
        notationCollector: NotationCollector = testErrorCollector(),
        trees: List<SourceContext> = parseText(notationCollector)
    ) {
        assert(notationCollector.hasNoIssues) { notationCollector.printReport() }

        trees.forEach {
            val globalContext = LucidGlobalContext(project, notationCollector)
            globalContext.walk(it)

            notationCollector.assertNoIssues()
        }
    }

    private suspend fun testBenchParse(
        notationCollector: NotationCollector = testErrorCollector(),
        trees: List<SourceContext> = parseText(notationCollector)
    ) {
        assert(notationCollector.hasNoIssues) { notationCollector.printReport() }

        trees.forEach {
            val testBenchContext = LucidTestBenchContext(project, notationCollector)
            testBenchContext.walk(it)

            notationCollector.assertNoIssues()
        }
    }

    suspend fun moduleTypeParse(
        notationCollector: NotationCollector = testErrorCollector(),
        trees: List<SourceContext> = parseText(notationCollector)
    ): List<Module> {
        assert(notationCollector.hasNoIssues) { notationCollector.printReport() }

        return trees.mapNotNull {
            val moduleTypeContext = LucidModuleTypeContext(project, notationCollector)
            val module = moduleTypeContext.extract(it)

            notationCollector.assertNoIssues()
            module
        }
    }

    /**
     * Performs a full parse on the file.
     * @param notationCollector if null, the function will automatically check for errors. If provided, you should check
     * for errors after calling this function.
     */
    suspend fun fullParse(notationCollector: NotationCollector? = null): ModuleInstance {
        val errors = notationCollector ?: testErrorCollector()
        val trees = parseText(errors)

        globalParse(errors, trees)
        val modules = moduleTypeParse(errors, trees)

        val moduleInstance = ModuleInstance("top", project, null, modules.first(), mapOf(), mapOf(), errors)

        moduleInstance.initialWalk()
        if (notationCollector == null)
            errors.assertNoIssues()

        return moduleInstance
    }

    suspend fun runFirstTestBench(notationCollector: NotationCollector? = null): SimParent {
        val errors = notationCollector ?: testErrorCollector()
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
            test.context.notationCollector.assertNoIssues()
        }
    }

    suspend fun runTestBenches(notationCollector: NotationCollector? = null) {
        val errors = notationCollector ?: testErrorCollector()
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
                it.context.notationCollector.assertNoIssues()
            }
        }
    }

    private suspend fun runTest(testBench: String, test: String) {
        val errors = testErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        moduleTypeParse(errors, tree)
        testBenchParse(errors, tree)

        val bench = project.getTestBenches().first { it.name == testBench }

        bench.initialWalk()

        errors.assertNoErrors()

        bench.runTest(test)
        bench.context.notationCollector.assertNoIssues()
    }

    suspend fun parallelRunTestBenches(notationCollector: NotationCollector? = null) {
        val errors = notationCollector ?: testErrorCollector()
        val tree = parseText(errors)

        globalParse(errors, tree)
        moduleTypeParse(errors, tree)
        testBenchParse(errors, tree)

        val testBenches = project.getTestBenches()

        testBenches.forEach { testBench ->
            testBench.initialWalk()
        }

        errors.assertNoErrors()

        coroutineScope {
            testBenches.forEach { testBench ->
                val tests = testBench.getTestBlocks()
                tests.forEach {
                    val tester = LucidTester(*files)
                    launch(Dispatchers.Default) {
                        tester.runTest(testBench.name, it.name)
                    }
                }
            }
        }
    }

    suspend fun getVerilog(notationCollector: NotationCollector? = null): Map<String, String> {
        val topInstance = fullParse(notationCollector)
        val instances = mutableMapOf<String, ModuleInstance>()
        fun add(instance: ModuleInstance) {
            instances[instance.parameterizedModuleName] = instance
            instance.context.types.moduleInstances.values.forEach { moduleInstanceOrArray ->
                when (moduleInstanceOrArray) {
                    is ModuleInstance -> add(moduleInstanceOrArray)
                    is ModuleInstanceArray -> moduleInstanceOrArray.modules.forEach { add(it) }
                }
            }
        }
        add(topInstance)
        return instances.mapValues { it.value.context.convertToVerilog() ?: error("Missing verilog for ${it.key}") }
    }
}
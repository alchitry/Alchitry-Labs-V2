package helpers

import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidGlobalContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidModuleTypeContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidTestBenchContext
import com.alchitry.labs2.parsers.hdl.lucid.signals.snapshot.SimParent
import com.alchitry.labs2.parsers.hdl.types.Module
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogModuleTypeContext
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.SourceFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.antlr.v4.kotlinruntime.ParserRuleContext

class ProjectTester(vararg val files: SourceFile) {
    val notationManager = NotationManager()
    val project = ProjectContext(notationManager, Board.AlchitryAuV2)

    suspend fun parseText() = Project.parseAll(files.toList(), notationManager) ?: error("Failed to parse all files!")

    suspend fun globalParse(
        trees: List<Pair<SourceFile, ParserRuleContext>>? = null
    ) {
        assert(notationManager.hasNoErrors) { notationManager.getReport() }

        (trees ?: parseText()).forEach {
            val globalContext = LucidGlobalContext(project, it.first)
            globalContext.walk(it.second)

            assert(notationManager.hasNoErrors) { notationManager.getReport() }
        }
    }

    private fun testBenchParse(
        trees: List<Pair<SourceFile, ParserRuleContext>>
    ) {
        assert(notationManager.hasNoErrors) { notationManager.getReport() }

        trees.forEach {
            val testBenchContext = LucidTestBenchContext(project, it.first)
            testBenchContext.walk(it.second)

            assert(notationManager.hasNoErrors) { notationManager.getReport() }
        }
    }

    suspend fun moduleTypeParse(
        trees: List<Pair<SourceFile, ParserRuleContext>>? = null
    ): List<Module> {
        assert(notationManager.hasNoErrors) { notationManager.getReport() }

        return (trees ?: parseText()).flatMap {
            val modules = when (it.first.language) {
                Languages.Lucid -> LucidModuleTypeContext(project, it.first).extract(it.second)
                Languages.Verilog -> VerilogModuleTypeContext(project, it.first).extract(it.second)
            }

            assert(notationManager.hasNoErrors) { notationManager.getReport() }
            modules
        }
    }

    /**
     * Performs a full parse on the file.
     * It does not check for errors after the parse.
     */
    suspend fun fullParse(mode: ExprEvalMode = ExprEvalMode.Default): ModuleInstance {
        val trees = parseText()

        globalParse(trees)
        val modules = moduleTypeParse(trees)

        val topModuleInstance = ModuleInstance("top", project, null, modules.first(), mapOf(), mapOf(), mapOf(), mode)

        topModuleInstance.initialWalk()

        modules.forEach { mod ->
            ModuleInstance(
                mod.name,
                project,
                null,
                mod,
                mapOf(),
                mapOf(),
                mapOf(),
                ExprEvalMode.Building
            ).apply {
                initialWalk()
            }
        }

        return topModuleInstance
    }

    suspend fun runFirstTestBench(): SimParent? {
        val tree = parseText()

        globalParse(tree)
        moduleTypeParse(tree)
        testBenchParse(tree)

        val testBenches = project.getTestBenches()

        val testBench = testBenches.first()
        testBench.initialWalk()
        val tests = testBench.getTestBlocks()

        val test = tests.first()
        return testBench.runTest(test.name).also {
            test.context.notationCollector.assertNoIssues()
        }
    }

    suspend fun runTestBenches() {
        val tree = parseText()

        globalParse(tree)
        moduleTypeParse(tree)
        testBenchParse(tree)

        val testBenches = project.getTestBenches()

        testBenches.forEach { testBench ->
            testBench.initialWalk()
        }

        assert(notationManager.hasNoErrors) { notationManager.getReport() }

        testBenches.forEach { testBench ->
            val tests = testBench.getTestBlocks()
            tests.forEach {
                testBench.runTest(it.name)
                it.context.notationCollector.assertNoIssues()
            }
        }
    }

    private suspend fun runTest(testBench: String, test: String) {
        val tree = parseText()

        globalParse(tree)
        moduleTypeParse(tree)
        testBenchParse(tree)

        val bench = project.getTestBenches().first { it.name == testBench }

        bench.initialWalk()

        assert(notationManager.hasNoErrors) { notationManager.getReport() }

        bench.runTest(test)
        bench.context.notationCollector.assertNoIssues()
    }

    suspend fun parallelRunTestBenches() {
        val tree = parseText()

        globalParse(tree)
        moduleTypeParse(tree)
        testBenchParse(tree)

        val testBenches = project.getTestBenches()

        testBenches.forEach { testBench ->
            testBench.initialWalk()
        }

        assert(notationManager.hasNoErrors) { notationManager.getReport() }

        coroutineScope {
            testBenches.forEach { testBench ->
                val tests = testBench.getTestBlocks()
                tests.forEach {
                    val tester = ProjectTester(*files)
                    launch(Dispatchers.Default) {
                        tester.runTest(testBench.name, it.name)
                    }
                }
            }
        }
    }

    suspend fun getVerilog(
        allowWarnings: Boolean = false
    ): Map<String, String> {
        val topInstance = fullParse()
        if (allowWarnings) {
            assertNoErrors()
        } else {
            assertNoIssues()
        }
        val usedModules = mutableMapOf<String, ModuleInstance>()
        fun add(instance: ModuleInstance) {
            usedModules[instance.module.name] = instance
            instance.context.types.moduleInstances.values.forEach { moduleInstanceOrArray ->
                when (moduleInstanceOrArray) {
                    is ModuleInstance -> add(moduleInstanceOrArray)
                    is ModuleInstanceArray -> moduleInstanceOrArray.modules.forEach { add(it) }
                }
            }
        }
        add(topInstance)
        return usedModules.mapValues {
            assert(project.notationManager.hasNoErrors) { project.notationManager.getReport() }
            it.value.module.convertToVerilog(project) ?: error("Missing verilog for ${it.key}")
        }.also { assert(project.notationManager.hasNoErrors) { project.notationManager.getReport() } }
    }

    fun assertNoErrors() = notationManager.assertNoErrors()
    fun assertNoIssues() = notationManager.assertNoIssues()
}
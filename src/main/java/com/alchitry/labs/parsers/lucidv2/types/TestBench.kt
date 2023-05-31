package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.TestBenchContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface TestOrModuleInstance {
    val name: String
}

class TestBench(
    override val name: String,
    project: ProjectContext,
    private val testBenchContext: TestBenchContext,
    errorCollector: ErrorCollector = ErrorCollector()
) : TestOrModuleInstance {
    val context = LucidBlockContext(project, this, errorCollector = errorCollector)

    fun initialWalk(): String? {
        val walkErrors = context.initialWalk(testBenchContext) ?: return null
        val sb = StringBuilder("Test instance $name contains errors:")
        walkErrors.forEach { sb.append("\n    ").append(it) }
        return sb.toString()
    }

    fun getTestBlocks(): List<TestBlock> {
        return context.blockParser.testBlocks.values.toList()
    }

    suspend fun runTest(name: String): Boolean = withContext(Dispatchers.IO) {
        val test = context.blockParser.testBlocks.values.firstOrNull { it.name == name } ?: return@withContext false
        test.context.initialize()
        try {
            test.evaluate()
        } catch (e: TestAbortedException) {
            return@withContext false
        }
        return@withContext true
    }
}

class TestAbortedException(override val message: String? = null) : RuntimeException()
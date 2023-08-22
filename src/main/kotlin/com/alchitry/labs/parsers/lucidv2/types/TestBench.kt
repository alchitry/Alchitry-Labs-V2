package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.grammar.LucidParser.TestBenchContext
import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.*
import com.alchitry.labs.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface TestOrModuleInstance {
    val name: String
}

class TestBench(
    override val name: String,
    project: Project,
    private val testBenchContext: TestBenchContext,
    errorCollector: ErrorCollector = ErrorCollector()
) : TestOrModuleInstance, Snapshotable {
    val context = LucidBlockContext(project, this, errorCollector = errorCollector)

    override fun takeSnapshot(): SnapshotParent {
        val snapshots = mutableListOf<SnapshotOrParent>()
        snapshots.addAll(context.types.dffs.values.map { it.takeSnapshot() })
        snapshots.addAll(context.types.sigs.values.map { it.takeSnapshot() })
        snapshots.addAll(context.types.moduleInstances.values.map { it.takeSnapshot() })
        return SnapshotParent(name, snapshots)
    }

    fun initialWalk(): String? {
        val walkErrors = context.initialWalk(testBenchContext) ?: return null
        val sb = StringBuilder("Test instance $name contains errors:")
        walkErrors.forEach { sb.append("\n    ").append(it) }
        return sb.toString()
    }

    fun getTestBlocks(): List<TestBlock> {
        return context.blockParser.testBlocks.values.toList()
    }

    suspend fun runTest(name: String): SimParent = withContext(Dispatchers.IO) {
        val test =
            context.blockParser.testBlocks.values.first { it.name == name }

        val snapshots = mutableListOf<SnapshotParent>()

        test.context.setSnapshotCallback { snapshots.add(takeSnapshot()) }

        test.context.initialize()
        try {
            test.evaluate()
        } catch (e: TestAbortedException) {
            return@withContext snapshotsToSimResult(snapshots)
        }
        return@withContext snapshotsToSimResult(snapshots)
    }
}

class TestAbortedException(override val message: String? = null) : RuntimeException()
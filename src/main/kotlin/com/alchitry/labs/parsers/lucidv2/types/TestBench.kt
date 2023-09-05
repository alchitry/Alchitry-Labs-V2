package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.parsers.grammar.LucidParser.TestBenchContext
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.*

sealed interface TestOrModuleInstance {
    val name: String
    val context: LucidBlockContext
}

class TestBench(
    override val name: String,
    project: ProjectContext,
    private val testBenchContext: TestBenchContext,
    errorCollector: ErrorCollector
) : TestOrModuleInstance, Snapshotable {
    override val context = LucidBlockContext(project, this, errorCollector = errorCollector)

    override fun takeSnapshot(): SnapshotParent {
        val snapshots = mutableListOf<SnapshotOrParent>()
        snapshots.addAll(context.types.dffs.values.map { it.takeSnapshot() })
        snapshots.addAll(context.types.sigs.values.map { it.takeSnapshot() })
        snapshots.addAll(context.types.moduleInstances.values.map { it.takeSnapshot() })
        return SnapshotParent(name, snapshots)
    }

    suspend fun initialWalk(): Boolean = context.initialWalk(testBenchContext)

    fun getTestBlocks(): List<TestBlock> {
        return context.blockParser.testBlocks.values.toList()
    }

    suspend fun runTest(name: String): SimParent {
        val test =
            context.blockParser.testBlocks.values.first { it.name == name }

        val snapshots = mutableListOf<SnapshotParent>()

        test.context.setSnapshotCallback { snapshots.add(takeSnapshot()) }

        test.context.initialize()
        try {
            test.evaluate()
        } catch (_: TestAbortedException) {
        }
        return snapshotsToSimResult(snapshots)
    }
}

class TestAbortedException(override val message: String? = null) : RuntimeException()
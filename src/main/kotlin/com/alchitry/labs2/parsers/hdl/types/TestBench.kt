package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser.TestBenchContext
import com.alchitry.labs2.parsers.hdl.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.lucidv2.signals.snapshot.*
import com.alchitry.labs2.project.files.SourceFile

sealed interface TestOrModuleInstance {
    val name: String
    val context: LucidBlockContext
}

class TestBench(
    override val name: String,
    val sourceFile: SourceFile,
    project: ProjectContext,
    context: TestBenchContext
) : TestOrModuleInstance, Snapshotable {
    private val testBenchContext: TestBenchContext = context//.deepCopy()

    override val context = LucidBlockContext(
        project,
        sourceFile,
        this,
        notationCollector = project.notationManager.getCollector(sourceFile)
    )

    fun getAllModules(): List<Module> {
        return context.types.moduleInstances.values.flatMap { instOrArray ->
            when (instOrArray) {
                is ModuleInstance -> instOrArray.getAllModules()
                is ModuleInstanceArray -> instOrArray.getAllInstances().first().getAllModules()
            }
        }.distinct()
    }

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

    suspend fun runTest(name: String): SimParent? {
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
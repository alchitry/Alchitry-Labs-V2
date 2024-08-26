package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.grammar.LucidParser.AlwaysBlockContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext

class AlwaysBlock(
    context: LucidBlockContext,
    dependencies: Set<Signal>,
    val drivenSignals: Set<Signal>,
    private val alwaysBlockContext: AlwaysBlockContext
): Evaluable {
    val context = context.withEvalContext(
        this,
        "AlwaysBlock in ${context.sourceFile.name} at line ${alwaysBlockContext.start?.line}"
    )

    init {
        drivenSignals.forEach {
            require(!it.hasDriver) { "Signal \"${it.name}\" is already driven!" }
            it.hasDriver = true
        }
        val evaluable = Evaluable {
            this@AlwaysBlock.context.project.queue(this@AlwaysBlock)
        }
        dependencies.forEach { it.addDependant(evaluable) }
    }

    override suspend fun evaluate() {
        context.walk(alwaysBlockContext)

        if (context.notationCollector.hasErrors) {
            context.notationCollector.printReport()
            error("Failed to evaluate always block!")
        }

        context.blockEvaluator.processWriteQueue()
    }
}
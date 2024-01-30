package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.grammar.LucidParser.AlwaysBlockContext
import com.alchitry.labs2.parsers.grammar.LucidParser.RepeatStatContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext

class AlwaysBlock(
    context: LucidBlockContext,
    dependencies: Set<Signal>,
    val drivenSignals: Set<Signal>,
    val repeatSignals: Map<RepeatStatContext, Signal>,
    private val alwaysBlockContext: AlwaysBlockContext
): Evaluable {
    val context = context.withEvalContext(this, "AlwaysBlock")

    init {
        dependencies.forEach { it.isRead = true }
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
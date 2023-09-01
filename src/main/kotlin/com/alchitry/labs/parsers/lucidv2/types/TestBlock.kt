package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.grammar.LucidParser.TestBlockContext
import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal

class TestBlock(
    val name: String,
    context: LucidBlockContext,
    dependencies: Set<Signal>,
    drivenSignals: Set<Signal>,
    private val testBlockContext: TestBlockContext
) : Evaluable {
    val context = context.withEvalContext(this, "TestBlock")

    init {
        dependencies.forEach { it.isRead = true }
        drivenSignals.forEach { it.hasDriver = true }
    }

    override suspend fun evaluate() {
        context.walk(testBlockContext)

        if (context.errorCollector.hasErrors) {
            context.errorCollector.printErrors()
            error("Failed to evaluate test block!")
        }

        context.blockEvaluator.processWriteQueue()
    }
}
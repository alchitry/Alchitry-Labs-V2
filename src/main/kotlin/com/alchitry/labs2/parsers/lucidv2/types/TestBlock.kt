package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.grammar.LucidParser.TestBlockContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext

class TestBlock(
    val name: String,
    context: LucidBlockContext,
    dependencies: Set<Signal>,
    drivenSignals: Set<Signal>,
    val testBlockContext: TestBlockContext
) : Evaluable {
    val context = context.withEvalContext(this, "TestBlock")

    init {
        drivenSignals.forEach { it.hasDriver = true }
    }

    override suspend fun evaluate() {
        context.walk(testBlockContext)

        if (context.notationCollector.hasErrors) {
            context.notationCollector.printReport()
            error("Failed to evaluate test block!")
        }

        context.blockEvaluator.processWriteQueue()
    }
}
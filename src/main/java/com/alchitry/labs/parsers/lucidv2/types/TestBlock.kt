package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.RepeatStatContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.TestBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal

class TestBlock(
    val name: String,
    context: LucidBlockContext,
    dependencies: Set<Signal>,
    val drivenSignals: Set<Signal>,
    val repeatSignals: Map<RepeatStatContext, Signal>,
    private val testBlockContext: TestBlockContext
) : Evaluable {
    val context = context.withEvalContext(this)

    init {
        dependencies.forEach { it.isRead = true }
        drivenSignals.forEach { it.hasDriver = true }
    }

    override suspend fun evaluate() {
        context.walk(testBlockContext)

        if (context.errorCollector.errors.isNotEmpty()) {
            context.errorCollector.errors.forEach { println(it) }
            error("Failed to evaluate test block!")
        }

        context.blockEvaluator.processWriteQueue()
    }
}
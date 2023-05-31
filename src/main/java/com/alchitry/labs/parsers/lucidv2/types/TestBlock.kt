package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.RepeatStatContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.TestBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class TestBlock(
    context: LucidModuleContext,
    dependencies: Set<Signal>,
    val drivenSignals: Set<Signal>,
    val repeatSignals: Map<RepeatStatContext, Signal>,
    private val testBlockContext: TestBlockContext
) : Evaluable {
    val context = context.withEvalContext(this)

    init {
        dependencies.forEach { it.isRead = true }
        drivenSignals.forEach {
            require(!it.hasDriver) { "Signal \"${it.name}\" is already driven!" }
            it.hasDriver = true
        }
        this.context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            onAnyChange(dependencies.map { it.valueFlow }) {
                this@TestBlock.context.project.queueEvaluation(this@TestBlock)
            }
        }
    }

    override suspend fun evaluate() {
        context.walk(testBlockContext)

        if (context.errorCollector.errors.isNotEmpty()) {
            context.errorCollector.errors.forEach { println(it) }
            error("Failed to evaluate test block!")
        }

        context.alwaysEvaluator.processWriteQueue()
    }
}
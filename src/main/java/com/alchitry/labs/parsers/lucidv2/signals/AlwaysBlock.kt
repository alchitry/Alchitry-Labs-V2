package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.AlwaysBlockContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.BlockContext
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class AlwaysBlock(
    context: LucidModuleContext,
    val dependencies: List<Signal>,
    val drivenSignals: List<Signal>,
    val repeatSignals: Map<BlockContext, Signal>,
    private val alwaysBlockContext: AlwaysBlockContext
): Evaluable {
    val context = context.withEvalContext(this)

    init {
        this.context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            onAnyChange(dependencies.map { it.valueFlow }) {
                this@AlwaysBlock.context.project.queueEvaluation(this@AlwaysBlock)
            }
        }
    }

    override suspend fun evaluate() {
        context.walk(alwaysBlockContext)

        if (context.errorCollector.errors.isNotEmpty()) {
            context.errorCollector.errors.forEach { println(it) }
            error("Failed to evaluate always block!")
        }

        context.alwaysEvaluator.processWriteQueue()
    }
}
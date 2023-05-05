package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.AlwaysBlockContext
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class AlwaysBlock(
    context: LucidModuleContext,
    dependencies: List<Signal>,
    private val alwaysBlockContext: AlwaysBlockContext
): Evaluable {
    private val context = context.withEvalContext(this)

    init {
        context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            onAnyChange(dependencies.map { it.valueFlow }) {
                context.project.queueEvaluation(this@AlwaysBlock)
            }
        }
    }

    override suspend fun evaluate() {
        context.walk(alwaysBlockContext)

        if (context.errorCollector.errors.isNotEmpty()) {
            context.errorCollector.errors.forEach { println(it) }
            error("Failed to evaluate always block!")
        }

        context.always.processWriteQueue()
    }
}
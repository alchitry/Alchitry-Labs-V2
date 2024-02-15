package com.alchitry.labs2.parsers.lucidv2.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidExprEval
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.values.ResolvableWidth
import com.alchitry.labs2.parsers.lucidv2.values.SignalWidth
import kotlinx.coroutines.runBlocking

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean,
    val context: LucidParser.PortDecContext
) {
    val isInout: Boolean = direction == SignalDirection.Both

    fun instantiate(parent: ModuleInstance, context: ProjectContext): PortInstance {
        val instWidth = if (width is ResolvableWidth) {
            val eval = LucidExprEval(
                context,
                parent.context.sourceFile,
                parent.context.notationCollector.createChild("PortEval")
            ) {
                return@LucidExprEval parent.parameters[it]
            }
            // this is OK as the eval will never suspend (no calls to $TICK and such)
            runBlocking { eval.walk(width.context, ignoreSkip = true) }
            eval.resolve(width.context) ?: error("Failed to resolve port width: $width")
        } else width
        return when (direction) {
            SignalDirection.Read -> Input(name, context, parent, instWidth, signed)
            SignalDirection.Write -> Output(name, context, parent, instWidth, signed)
            SignalDirection.Both -> Inout(name, parent, instWidth, signed)
        }
    }
}
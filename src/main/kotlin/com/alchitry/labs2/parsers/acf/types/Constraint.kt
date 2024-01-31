package com.alchitry.labs2.parsers.acf.types

import com.alchitry.labs2.hardware.pinout.Pin
import com.alchitry.labs2.parsers.grammar.AcfParser
import com.alchitry.labs2.parsers.lucidv2.types.SignalOrSubSignal
import org.antlr.v4.kotlinruntime.ParserRuleContext

sealed interface Constraint {
    val pin: Pin
    val port: SignalOrSubSignal
    val pull: PinPull?
    val context: ParserRuleContext
}

data class PinConstraint(
    override val pin: Pin,
    override val port: SignalOrSubSignal,
    override val pull: PinPull?,
    override val context: AcfParser.PinContext
) : Constraint

data class ClockConstraint(
    override val pin: Pin,
    override val port: SignalOrSubSignal,
    override val pull: PinPull?,
    val frequency: Int,
    override val context: AcfParser.ClockContext
) : Constraint

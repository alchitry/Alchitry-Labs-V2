package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs2.parsers.lucidv2.values.Value

/**
 * A signal whose reads are delegated to a proxy. This is used by inout ports on modules since the read value
 * depends on two different signal values.
 */
class ProxySignal(
    name: String,
    direction: SignalDirection,
    parent: SignalParent?,
    initialValue: Value,
    signed: Boolean = initialValue is SimpleValue && initialValue.signed,
    private val onRead: (evalContext: Evaluable?) -> Value
) : Signal(name, direction, parent, initialValue, signed) {

    override fun read(evalContext: Evaluable?): Value = onRead(evalContext)
    fun baseRead(evalContext: Evaluable?) = super.read(evalContext)

    fun addWriteDependant(evaluable: Evaluable) {
        super.addDependant(evaluable)
    }

    private var proxyDependants = mutableSetOf<Evaluable>()

    override fun addDependant(dependant: Evaluable) {
        proxyDependants.add(dependant)
    }

    override fun removeDependant(dependant: Evaluable) {
        proxyDependants.remove(dependant)
    }

    suspend fun updateRead() {
        proxyDependants.forEach { it.evaluate() }
    }
}
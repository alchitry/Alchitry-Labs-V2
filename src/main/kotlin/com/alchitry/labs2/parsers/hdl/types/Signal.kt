package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.signals.snapshot.Snapshot
import com.alchitry.labs2.parsers.hdl.lucid.signals.snapshot.Snapshotable
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import com.alchitry.labs2.parsers.hdl.values.Value
import kotlinx.coroutines.sync.Mutex

open class Signal(
    override val name: String, // includes namespace or module name
    override val direction: SignalDirection,
    override val parent: SignalParent?,
    val initialValue: Value,
    override val type: ExprType,
    val signed: Boolean = initialValue is SimpleValue && initialValue.signed,
) : SignalOrSubSignal, SignalOrParent, Snapshotable {
    fun select(selection: SignalSelection) = SubSignal(this, selection)

    private var dependants = mutableSetOf<Evaluable>()

    private var setEvalContext: Evaluable? = null
    private var nextValue: Value? = null
    private var value: Value = initialValue.withSign(signed)

    val subSignalLock = Mutex() // lock used by sub-signals during read-modify-write cycles

    var hasDriver: Boolean = false
    var isRead: Boolean = false
        private set

    override fun addDependant(dependant: Evaluable) {
        dependants.add(dependant)
    }

    override fun removeDependant(dependant: Evaluable) {
        dependants.remove(dependant)
    }

    override fun takeSnapshot() = Snapshot(name, read())

    override fun read(evalContext: Evaluable?): Value {
        if (evalContext === setEvalContext)
            return nextValue ?: value
        return value
    }

    fun markRead() {
        isRead = true
    }

    override val width: SignalWidth = value.width

    override fun quietWrite(v: Value, evalContext: Evaluable?) {
        require(setEvalContext == null || evalContext === setEvalContext) { "Attempted to set signal from two different evalContext!" }
        setEvalContext = evalContext
        quietWrite(v)
    }

    private fun quietWrite(v: Value) {
        require(value.canAssign(v)) { "Signal assigned value does not match its size!" }
        val resizedValue = v.resizeToMatch(value.width)
        nextValue = resizedValue.withSign(signed)
    }

    var lastValue: Value? = null
    override suspend fun publish() {
        nextValue?.let {
            value = it
            if (lastValue != it) { // only do something on a change
                lastValue = it
                dependants.forEach { evaluable -> evaluable.evaluate() }
            }
        }
        nextValue = null
        setEvalContext = null
    }

    override suspend fun write(v: Value) {
        quietWrite(v)
        publish()
    }

    fun fullName(): String {
        val parts = mutableListOf<String>()
        var current: SignalOrParent = this
        parts.add(this.name)
        while (current.parent != null && current.parent !is LocalSignal) {
            parts.add(current.parent!!.name)
            current = current.parent!!
        }
        return parts.asReversed().joinToString(".")
    }

    override fun toString(): String = fullName()

}
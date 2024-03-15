package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.lucidv2.values.*
import kotlinx.coroutines.sync.withLock

data class SubSignal(
    val parent: Signal,
    val selection: SignalSelection
) : SignalOrSubSignal {
    override fun read(evalContext: Evaluable?): Value {
        return parent.read(evalContext).select(selection)
    }

    override val width: SignalWidth = read().width

    override val direction: SignalDirection
        get() = parent.direction

    private var dependants = mutableSetOf<Evaluable>()
    private var lastValue: Value? = null
    private val evaluable = object : Evaluable {
        override suspend fun evaluate() {
            val current = read()
            if (current != lastValue) {
                dependants.forEach { it.evaluate() }
                lastValue = current
            }
        }
    }

    override fun addDependant(dependant: Evaluable) {
        if (dependants.isEmpty()) { // only add this as a dependant if we have dependants
            parent.addDependant(evaluable)
        }
        dependants.add(dependant)
    }

    override fun removeDependant(dependant: Evaluable) {
        dependants.remove(dependant)
        if (dependants.isEmpty())
            parent.removeDependant(evaluable)
    }

    /**
     * Generates the full value for the parent signal with the value v applied to the selected portion of the signal.
     */
    private fun getFullValue(v: Value, evalContext: Evaluable?): Value {
        require(width.canAssign(v.width)) {
            "Cannot set value $v to selected subsignal!"
        }
        val current = parent.read(evalContext)
        return current.write(selection, v.resizeToMatch(width))
    }

    // this performs a read-modify-write just like write() does, but we can assume it will always be from the same thread
    // so no need for synchronization
    override fun quietWrite(v: Value, evalContext: Evaluable?) =
        parent.quietWrite(getFullValue(v, evalContext), evalContext)

    // write performs a read-modify-write cycle on the underling signal so the subSignalLock is required to prevent
    // corruption as this could be called from multiple threads
    override suspend fun write(v: Value) = parent.subSignalLock.withLock { parent.write(getFullValue(v, null)) }

    // this also shouldn't need synchronization because we will either use write() or quietWrite()/publish() exclusively
    // we should never be mixing calls to write() with calls to publish()
    override suspend fun publish() = parent.publish()

    /**
     * Returns the offset and bits selected by this [SubSignal].
     * This is used in the Verilog translations.
     */
    val flatSelectionData: FlatSelectionData
        get() {
            var currentWidth: SignalWidth? = parent.width
            check(currentWidth!!.isDefined()) { "selectionOffset only works on well defined signal widths!" }
            var offset = 0
            var selectedBits = currentWidth.bitCount!!
            for (selector in selection) {
                when (currentWidth) {
                    is DefinedArrayWidth, is DefinedSimpleWidth -> {
                        val s = selector as? SignalSelector.Bits ?: error("Struct selector used on an array!")
                        val elementSize = ((currentWidth as? DefinedArrayWidth)?.next?.bitCount ?: 1)
                        offset += s.range.first * elementSize
                        selectedBits = s.count * elementSize
                        currentWidth = if (currentWidth is DefinedArrayWidth)
                            when (s.context) {
                                is SelectionContext.Constant,
                                is SelectionContext.Single ->
                                    currentWidth.next

                                is SelectionContext.Fixed,
                                is SelectionContext.DownTo,
                                is SelectionContext.UpTo ->
                                    null // these select a range of bits and can't be selected further
                            } else null
                    }

                    is StructWidth -> {
                        val s = selector as? SignalSelector.Struct ?: error("Bit selector used on a struct!")
                        val type = currentWidth.type
                        val member = type[s.member] ?: error("Struct member could not be found!")
                        offset += type.offsetOf(s.member) ?: error("Struct member offset not well defined!")
                        currentWidth = member.width
                        selectedBits = currentWidth.bitCount!!
                    }

                    is UndefinedWidth -> error("Undefined width in flatSelectionData!")
                    null -> error("Too many selectors for the signal width!")
                }
            }
            return FlatSelectionData(offset, selectedBits)
        }

    override fun toString() = getSignal().toString() + selection.joinToString { it.toString() }
}

data class FlatSelectionData(val offset: Int, val bits: Int)
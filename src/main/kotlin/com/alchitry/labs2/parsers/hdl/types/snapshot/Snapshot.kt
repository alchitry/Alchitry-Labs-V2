package com.alchitry.labs2.parsers.hdl.lucid.signals.snapshot

import com.alchitry.labs2.parsers.hdl.values.Value

sealed interface SnapshotOrParent {
    val name: String
}

data class Snapshot(override val name: String, val value: Value) : SnapshotOrParent

data class SnapshotParent(override val name: String, val children: List<SnapshotOrParent>) : SnapshotOrParent,
    Map<String, SnapshotOrParent> by children.associateBy({ it.name })

interface Snapshotable {
    fun takeSnapshot(): SnapshotOrParent
}
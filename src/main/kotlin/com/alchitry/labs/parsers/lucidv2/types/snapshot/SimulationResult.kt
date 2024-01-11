package com.alchitry.labs.parsers.lucidv2.signals.snapshot

import com.alchitry.labs.parsers.lucidv2.values.Value

fun snapshotsToSimResult(snapshots: List<SnapshotParent>): SimParent? {
    val result = buildEmptyTree(snapshots.firstOrNull() ?: return null) as SimParent

    snapshots.forEach { snapshot ->
        addToTree(result, snapshot)
    }

    return result
}

private fun addToTree(tree: SimValueOrParent, snap: SnapshotOrParent) {
    when (snap) {
        is Snapshot -> {
            check(tree is SimValue)
            (tree.values as MutableList<Value>).add(snap.value)
        }

        is SnapshotParent -> {
            check(tree is SimParent)
            snap.children.forEach {
                addToTree(tree[it.name]!!, it)
            }
        }
    }
}

private fun buildEmptyTree(snap: SnapshotOrParent): SimValueOrParent {
    return when (snap) {
        is Snapshot -> SimValue(snap.name, mutableListOf())
        is SnapshotParent -> SimParent(snap.name, snap.children.map { buildEmptyTree(it) })
    }
}

sealed interface SimValueOrParent {
    val name: String
    fun valueString(): String
}

data class SimValue(override val name: String, val values: List<Value>) : SimValueOrParent, List<Value> by values {
    override fun valueString() = "$name: $values"
}

data class SimParent(override val name: String, val children: List<SimValueOrParent>) : SimValueOrParent,
    Map<String, SimValueOrParent> by children.associateBy({ it.name }) {
    override fun valueString() = children.joinToString("\n") { "$name.${it.valueString()}" }
}
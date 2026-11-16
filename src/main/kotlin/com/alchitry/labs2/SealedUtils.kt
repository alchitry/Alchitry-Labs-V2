package com.alchitry.labs2

import com.alchitry.hardware.Board
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.allSealedObjects(): List<T> {
    val objects = mutableListOf<T>()
    addSealedObjectsToList(objects)
    return objects
}

private fun <T : Any> KClass<T>.addSealedObjectsToList(list: MutableList<in T>) {
    sealedSubclasses.forEach { subclass ->
        subclass.objectInstance?.let { list.add(it) }
        subclass.addSealedObjectsToList(list)
    }
}

fun Board.Companion.resolveAliases(name: String): List<Board> {
    return when (name) {
        "All" -> Board.All
        "V2" -> listOf(
            Board.AlchitryCuV2,
            Board.AlchitryAuV2T35,
            Board.AlchitryAuV2T50,
            Board.AlchitryPtV2T100,
            Board.AlchitryPtV2T200
        )

        "AuV2" -> listOf(
            Board.AlchitryAuV2T35,
            Board.AlchitryAuV2T50
        )

        "PtV2" -> listOf(
            Board.AlchitryPtV2T100,
            Board.AlchitryPtV2T200
        )

        "Xilinx" -> listOf(
            Board.AlchitryAu,
            Board.AlchitryAuPlus,
            Board.AlchitryAuV2T35,
            Board.AlchitryAuV2T50,
            Board.AlchitryPtV2T100,
            Board.AlchitryPtV2T200
        )

        else -> listOf(Board.fromName(name) ?: error("Unknown board \"$name\""))
    }
}

fun Board.Companion.resolveAliases(names: Iterable<String>): List<Board> {
    return names.flatMap { resolveAliases(it) }.distinct()
}
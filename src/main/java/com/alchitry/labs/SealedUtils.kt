package com.alchitry.labs

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
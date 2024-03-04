package com.alchitry.labs2

fun <T : Any> List<T?>.noNulls(): List<T>? {
    if (any { it == null })
        return null
    @Suppress("UNCHECKED_CAST")
    return this as List<T>
}
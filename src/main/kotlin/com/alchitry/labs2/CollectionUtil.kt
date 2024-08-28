package com.alchitry.labs2

/**
 * If the list contains no nulls, this casts it to a non-null variant. Otherwise, it returns null.
 *
 * @return a non-null cast of the original list or null if the original contained any nulls.
 */
fun <T : Any> List<T?>.noNulls(): List<T>? {
    if (any { it == null })
        return null
    @Suppress("UNCHECKED_CAST")
    return this as List<T>
}
package com.alchitry.labs2

fun String.prefixLinesWith(prefix: String): String = lines().joinToString("\n") { prefix + it }
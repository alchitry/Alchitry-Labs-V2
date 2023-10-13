package com.alchitry.labs

fun String.prefixLinesWith(prefix: String): String = lines().joinToString("\n") { prefix + it }
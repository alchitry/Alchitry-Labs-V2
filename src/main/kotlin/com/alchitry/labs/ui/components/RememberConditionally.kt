package com.alchitry.labs.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember

private class Wrapper<T>(var value: T)

@Composable
fun <T> rememberConditionally(condition: Boolean, block: @DisallowComposableCalls () -> T): T? {
    val wrapper = remember { Wrapper<T?>(null) }

    if (condition)
        wrapper.value = block()

    return wrapper.value
}
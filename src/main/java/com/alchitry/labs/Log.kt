package com.alchitry.labs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.alchitry.labs.ui.main.Console

object Log {
    fun showError(message: String, throwable: Throwable? = null) {
        printError(message, throwable)
        // TODO: Make this show a dialog
    }

    fun println(message: String, style: SpanStyle? = null) {
        Console.append(message, style)
        kotlin.io.println(message)
    }

    fun printError(message: String, throwable: Throwable? = null) {
        println(message, SpanStyle(color = Color.Red))
        throwable?.let {
            println(it.message)
            it.printStackTrace()
        }
    }
}
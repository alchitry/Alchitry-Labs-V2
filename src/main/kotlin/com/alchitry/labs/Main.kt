package com.alchitry.labs

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.awt.ComposeWindow
import com.alchitry.labs.subcommands.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlin.reflect.full.declaredFunctions

val LocalScale = compositionLocalOf { 1.0f }


val LocalComposeWindow = compositionLocalOf<ComposeWindow> { error("No ComposeWindow set!") }
lateinit var mainWindow: ComposeWindow

fun ArgParser.showHelp(error: String? = null) {
    error?.let { System.err.println("Error: $it") }
    ArgParser::class.declaredFunctions.find { it.name == "makeUsage" }?.let {
        System.err.println(it.call(this))
    }
}

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    if (Env.os == Env.OS.UNKNOWN) {
        System.err.println("Warning: OS detection failed!")
    }

    val parser = ArgParser("alchitry_labs", strictSubcommandOptionsOrder = true)

    parser.subcommands(
        CreateProject(),
        CloneProject(),
        CheckProject(),
        BuildProject(),
        LoadProject(),
        SimulateProject(),
        Labs(),
        Loader()
    )

    parser.parse(args)

    if (args.isEmpty()) {
        parser.showHelp("A subcommand must be specified!")
    }
}

package com.alchitry.labs2

import com.alchitry.labs2.subcommands.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import kotlin.reflect.full.declaredFunctions

fun ArgParser.showHelp(error: String? = null) {
    error?.let { Log.printlnError("Error: $it") }
    ArgParser::class.declaredFunctions.find { it.name == "makeUsage" }?.let {
        Log.println(it.call(this))
    }
}

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {

    if (Env.os == Env.OS.Unknown) {
        System.err.println("Warning: OS detection failed!")
    }

    val parser = ArgParser("alchitry", strictSubcommandOptionsOrder = true)

    parser.subcommands(
        CreateProject(),
        CloneProject(),
        CheckProject(),
        BuildProject(),
        LoadProject(),
        SimulateProject()
    )

    parser.parse(args)

    if (args.isEmpty()) {
        parser.showHelp("A subcommand must be specified!")
    }
}

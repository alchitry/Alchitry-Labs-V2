package com.alchitry.labs.subcommands

import com.alchitry.labs.showHelp
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@OptIn(ExperimentalCli::class)
class Loader : Subcommand("loader", "Launch Alchitry Loader GUI") {
    override fun execute() {
        showHelp("Not yet implemented!")
    }
}
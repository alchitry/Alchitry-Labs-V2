package com.alchitry.labs

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand

@OptIn(ExperimentalCli::class)
class Loader : Subcommand("loader", "Launch Alchitry Loader GUI") {
    override fun execute() {
        showHelp("Not yet implemented!")
    }
}
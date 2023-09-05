package com.alchitry.labs.subcommands

import com.alchitry.labs.parsers.errors.ErrorManager
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.coroutines.runBlocking
import java.io.File

@OptIn(ExperimentalCli::class)
class SimulateProject : Subcommand("sim", "Simulate a project") {
    private val project by argument(ArgType.String, "project", "Alchitry project file")
    private val list by option(ArgType.Boolean, "list", "l", "List test benches").default(false)
    private val tests by option(ArgType.String, "tests", "t", "Comma seperated list of tests to run")

    override fun execute() {
        val project = try {
            Project.openXml(File(project))
        } catch (e: Exception) {
            System.err.println("Failed to open project:")
            System.err.println("     ${e.message}")
            return
        }

        val errorManager = ErrorManager()
        val projectContext = runBlocking { project.parse(errorManager) }

        if (projectContext == null) {
            println("Failed to fully parse project!")
            print(errorManager.getReport())
            return
        }

        val testBenches = projectContext.getTestBenches()

        if (list) {
            if (testBenches.isEmpty()) {
                println("No tests found!")
            } else {
                println("Tests:")
                testBenches.forEach { testBench ->
                    testBench.getTestBlocks().forEach {
                        println("    ${testBench.name}.${it.name}")
                    }
                }
            }

            return
        }

        val tests = tests
        if (tests == null) {
            testBenches.forEach { testBench ->
                testBench.getTestBlocks().forEach { test ->
                    runBlocking {
                        val ec = testBench.context.errorCollector
                        testBench.runTest(test.name)
                        if (ec.hasErrors) {
                            println("Test ${testBench.name}.${test.name} failed:")
                            ec.getAllNotations().forEach {
                                println("    $it")
                            }
                            ec.clear()
                        } else {
                            println("Test ${testBench.name}.${test.name} passed!")
                        }
                    }
                }
            }
        }
    }
}
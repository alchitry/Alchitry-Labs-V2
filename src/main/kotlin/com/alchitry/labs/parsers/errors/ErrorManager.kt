package com.alchitry.labs.parsers.errors

import com.alchitry.labs.project.files.ProjectFile

class ErrorManager {
    private val errorCollectors = mutableMapOf<ProjectFile, ErrorCollector>()

    fun getCollector(file: ProjectFile): ErrorCollector {
        return errorCollectors.getOrPut(file) { ErrorCollector(file) }
    }

    fun getReport(): String {
        val stringBuilder = StringBuilder()

        errorCollectors.values.forEach { collector ->
            collector.getReport()?.let { report ->
                stringBuilder.append(report)
                stringBuilder.append("\n")
            }
        }

        if (stringBuilder.isEmpty())
            return "No issues detected.\n"

        return stringBuilder.toString()
    }

    val hasNoMessages: Boolean get() = errorCollectors.values.all { it.hasNoMessages }
    val hasNoErrors: Boolean get() = errorCollectors.values.all { it.hasNoErrors }
    val hasErrors: Boolean get() = !hasNoErrors
}
package com.alchitry.labs.parsers.lucidv2

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

        return stringBuilder.toString()
    }

    val hasNoIssues: Boolean get() = errorCollectors.values.all { it.hasNoIssues }
    val hasNoErrors: Boolean get() = errorCollectors.values.all { it.hasNoErrors }
    val hasErrors: Boolean get() = !hasNoErrors
}
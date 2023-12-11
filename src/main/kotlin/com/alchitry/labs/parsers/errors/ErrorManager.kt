package com.alchitry.labs.parsers.errors

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.alchitry.labs.project.files.ProjectFile
import com.alchitry.labs.ui.theme.AlchitryColors

class ErrorManager {
    private val errorCollectors = mutableMapOf<ProjectFile, ErrorCollector>()

    fun getCollector(file: ProjectFile): ErrorCollector {
        return errorCollectors.getOrPut(file) { ErrorCollector(file) }
    }

    fun getReport(): AnnotatedString {
        return AnnotatedString.Builder().apply {
            errorCollectors.values.forEach { collector ->
                collector.getReport()?.let { report ->
                    appendLine(report)
                }
            }

            if (this.length == 0)
                withStyle(SpanStyle(color = AlchitryColors.current.Success)) {
                    appendLine("No issues detected.")
                }

        }.toAnnotatedString()
    }

    val hasNoMessages: Boolean get() = errorCollectors.values.all { it.hasNoMessages }
    val hasNoErrors: Boolean get() = errorCollectors.values.all { it.hasNoErrors }
    val hasErrors: Boolean get() = !hasNoErrors
}
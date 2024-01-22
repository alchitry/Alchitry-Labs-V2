package com.alchitry.labs2.parsers.notations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.ui.theme.AlchitryColors

class NotationManager {
    private val notationCollectors = mutableMapOf<ProjectFile, NotationCollector>()

    fun getCollector(file: ProjectFile): NotationCollector {
        return notationCollectors.getOrPut(file) { NotationCollector(file) }
    }

    fun getReport(): AnnotatedString {
        return AnnotatedString.Builder().apply {
            notationCollectors.values.forEach { collector ->
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

    val hasNoMessages: Boolean get() = notationCollectors.values.all { it.hasNoMessages }
    val hasNoErrors: Boolean get() = notationCollectors.values.all { it.hasNoErrors }
    val hasNoWarnings: Boolean get() = notationCollectors.values.all { it.hasNoWarnings }
    val hasNoIssues: Boolean get() = notationCollectors.values.all { it.hasNoIssues }
    val hasErrors: Boolean get() = !hasNoErrors
    val hasWarnings: Boolean get() = !hasNoWarnings
}
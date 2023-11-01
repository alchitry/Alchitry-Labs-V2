package com.alchitry.labs.ui.tree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.fillMaxIntrinsic
import com.alchitry.labs.ui.selection.SingleSelectionContext

@Composable
fun ProjectTree() {
    val project = Project.currentFlow.collectAsState().value ?: return
    val selectionContext = remember { SingleSelectionContext() }

    with(selectionContext) {
        Row(
            Modifier
                .fillMaxWidth()
                //.horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState())
        ) {
            Column(Modifier.fillMaxIntrinsic()) {
                TreeSection(project.projectName, 0) {
                    TreeSection("Source Files", 1) {
                        project.sourceFiles.forEach {
                            TreeItem(it.name, 2)
                        }
                    }
                }
            }
        }
    }
}
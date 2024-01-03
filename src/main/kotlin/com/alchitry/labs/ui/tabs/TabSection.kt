package com.alchitry.labs.ui.tabs

import androidx.compose.runtime.Composable
import com.alchitry.labs.ui.drag_and_drop.DragAndDropContext

sealed class TabSection(var parent: TabParent) {
    abstract fun getTabs(): List<Tab>
    abstract fun closeAll()

    context(DragAndDropContext<Tab>)
    @Composable
    abstract fun content()
}
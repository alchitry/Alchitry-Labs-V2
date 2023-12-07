package com.alchitry.labs.ui.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.alchitry.labs.ui.components.HSash
import com.alchitry.labs.ui.components.VSash
import com.alchitry.labs.ui.drag_and_drop.DragAndDropContext
import com.alchitry.labs.ui.drag_and_drop.DragAndDropZone

class TabManager : TabParent {
    private var tabSection by mutableStateOf<TabSection>(TabPanel(this))

    override fun replaceTabSection(original: TabSection, new: TabSection) {
        if (original === tabSection)
            tabSection = new
    }

    override fun activeTabPanel(): TabPanel {
        return when (val section = tabSection) {
            is TabParent -> section.activeTabPanel()
            is TabPanel -> section
        }
    }

    override fun getTabs(): List<Tab> {
        return tabSection.getTabs()
    }

    @Composable
    fun content() {
        DragAndDropZone {
            tabSection.content()
        }
    }
}

sealed interface TabParent {
    fun replaceTabSection(original: TabSection, new: TabSection)
    fun activeTabPanel(): TabPanel
    fun getTabs(): List<Tab>
}

sealed class TabSection(protected var parent: TabParent) {
    abstract fun getTabs(): List<Tab>

    context(DragAndDropContext<Tab>)
    @Composable
    abstract fun content()
}

class HorizontalSplit(
    parent: TabParent,
    left: TabSection?,
    right: TabSection?
) : TabSection(parent), TabParent {
    private var left by mutableStateOf(left ?: TabPanel(this))
    private var right by mutableStateOf(right ?: TabPanel(this))

    override fun getTabs(): List<Tab> = left.getTabs() + right.getTabs()

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        HSash(left = { left.content() }, right = { right.content() })
    }

    override fun replaceTabSection(original: TabSection, new: TabSection) {
        if (original === left) {
            left = new
            return
        }
        if (original === right)
            right = new
    }

    override fun activeTabPanel(): TabPanel {
        return when (val section = left) { // TODO: keep track of last used panel
            is TabParent -> section.activeTabPanel()
            is TabPanel -> section
        }
    }
}

class VerticalSplit(
    parent: TabParent,
    top: TabSection?,
    bottom: TabSection?
) : TabSection(parent), TabParent {
    private var top by mutableStateOf(top ?: TabPanel(this))
    private var bottom by mutableStateOf(bottom ?: TabPanel(this))

    override fun getTabs(): List<Tab> = top.getTabs() + bottom.getTabs()

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        VSash(top = { top.content() }, bottom = { bottom.content() })
    }

    override fun replaceTabSection(original: TabSection, new: TabSection) {
        if (original === top) {
            top = new
            return
        }
        if (original === bottom)
            bottom = new
    }

    override fun activeTabPanel(): TabPanel {
        return when (val section = top) { // TODO: keep track of last used panel
            is TabParent -> section.activeTabPanel()
            is TabPanel -> section
        }
    }
}

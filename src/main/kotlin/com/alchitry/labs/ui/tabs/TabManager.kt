package com.alchitry.labs.ui.tabs

import androidx.compose.runtime.*
import com.alchitry.labs.ui.components.HSash
import com.alchitry.labs.ui.components.VSash
import com.alchitry.labs.ui.drag_and_drop.DragAndDropContext
import com.alchitry.labs.ui.drag_and_drop.DragAndDropZone

class TabManager : TabParent {
    private var tabSection by mutableStateOf<TabSection>(TabPanel(this))

    override fun replaceTabSection(original: TabSection, new: TabSection?) {
        if (new == null) {
            return
        }
        if (original !== tabSection)
            error("original didn't match section")
        tabSection = new
        new.parent = this
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
        key(this) {
            DragAndDropZone {
                tabSection.content()
            }
        }
    }
}

sealed interface TabParent {
    fun replaceTabSection(original: TabSection, new: TabSection?)
    fun activeTabPanel(): TabPanel
    fun getTabs(): List<Tab>
}

sealed class TabSection(var parent: TabParent) {
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
    var left by mutableStateOf(left ?: TabPanel(this))
    var right by mutableStateOf(right ?: TabPanel(this))

    init {
        left?.parent = this
        right?.parent = this
    }

    override fun getTabs(): List<Tab> = left.getTabs() + right.getTabs()

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        key(this) {
            HSash(left = { left.content() }, right = { right.content() })
        }
    }

    override fun replaceTabSection(original: TabSection, new: TabSection?) {
        if (new == null) {
            if (original === left)
                parent.replaceTabSection(this, right)
            else if (original === right)
                parent.replaceTabSection(this, left)
            return
        }

        new.parent = this
        if (original === left) {
            left = new
            return
        }
        if (original === right) {
            right = new
            return
        }
        error("original didn't match left or right")
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
    var top by mutableStateOf(top ?: TabPanel(this))
    var bottom by mutableStateOf(bottom ?: TabPanel(this))

    init {
        top?.parent = this
        bottom?.parent = this
    }

    override fun getTabs(): List<Tab> = top.getTabs() + bottom.getTabs()

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        key(this) {
            VSash(top = { top.content() }, bottom = { bottom.content() })
        }
    }

    override fun replaceTabSection(original: TabSection, new: TabSection?) {
        if (new == null) {
            if (original === top)
                parent.replaceTabSection(this, bottom)
            else if (original === bottom)
                parent.replaceTabSection(this, top)
            return
        }

        new.parent = this
        if (original === top) {
            top = new
            return
        }
        if (original === bottom) {
            bottom = new
            return
        }
        error("original didn't match top or bottom")
    }

    override fun activeTabPanel(): TabPanel {
        return when (val section = top) { // TODO: keep track of last used panel
            is TabParent -> section.activeTabPanel()
            is TabPanel -> section
        }
    }
}

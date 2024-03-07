package com.alchitry.labs2.ui.tabs

import androidx.compose.runtime.*
import com.alchitry.labs2.ui.components.HSash
import com.alchitry.labs2.ui.components.VSash
import com.alchitry.labs2.ui.drag_and_drop.DragAndDropContext

class HorizontalSplit(
    parent: TabParent,
    left: TabSection?,
    right: TabSection?
) : Split(parent, left, right) {
    var left by this::first
    var right by this::second


    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        key(this) {
            HSash(left = { left.content() }, right = { right.content() })
        }
    }
}

class VerticalSplit(
    parent: TabParent,
    top: TabSection?,
    bottom: TabSection?
) : Split(parent, top, bottom) {
    var top by this::first
    var bottom by this::second

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        key(this) {
            VSash(top = { top.content() }, bottom = { bottom.content() })
        }
    }
}

abstract class Split(
    parent: TabParent,
    first: TabSection?,
    second: TabSection?
) : TabSection(parent), TabParent {
    var first by mutableStateOf(first ?: TabPanel(this))
    var second by mutableStateOf(second ?: TabPanel(this))
    private var active: TabSection = this.first

    init {
        first?.parent = this
        second?.parent = this
    }

    override fun getTabs(): List<Tab> = first.getTabs() + second.getTabs()

    override fun closeAll() {
        first.closeAll()
        second.closeAll()
    }

    override fun replaceTabSection(original: TabSection, new: TabSection?) {
        if (new == null) {
            if (original === first)
                parent.replaceTabSection(this, second)
            else if (original === second)
                parent.replaceTabSection(this, first)
            return
        }

        new.parent = this
        if (original === first) {
            first = new
            return
        }
        if (original === second) {
            second = new
            return
        }
        error("original didn't match first or second")
    }

    override fun setActiveSection(tab: TabSection) {
        check(tab === first || tab === second) { "setActive called with a tab that doesn't belong!" }
        active = tab
    }

    override fun activeTabPanel(): TabPanel {
        return when (val section = active) {
            is TabParent -> section.activeTabPanel()
            is TabPanel -> section
            //  else -> error("IMPOSSIBLE") // TODO: remove - added to avoid a compiler bug
        }
    }
}

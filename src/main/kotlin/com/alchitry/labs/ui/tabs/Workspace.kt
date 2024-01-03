package com.alchitry.labs.ui.tabs

import androidx.compose.runtime.*
import com.alchitry.labs.project.files.ProjectFile
import com.alchitry.labs.ui.drag_and_drop.DragAndDropZone

data object Workspace : TabParent {
    private var tabSection by mutableStateOf<TabSection>(TabPanel(this))

    fun openFile(
        file: ProjectFile
    ) {
        getTabs().firstOrNull { it is FileTab && it.file == file }?.let {
            it.parent.focusTab(it)
            return
        }

        val panel = activeTabPanel()
        panel.addTab(FileTab(file, panel))
    }

    override fun closeAll() {
        tabSection.closeAll()
        tabSection = TabPanel(this)
    }

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

    override fun setActiveSection(tab: TabSection) {
        check(tab === tabSection) { "setActiveSection called with mismatch tab!" }
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
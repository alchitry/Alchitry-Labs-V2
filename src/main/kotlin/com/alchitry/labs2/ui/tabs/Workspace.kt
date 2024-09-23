package com.alchitry.labs2.ui.tabs

import androidx.compose.runtime.*
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.ui.drag_and_drop.DragAndDropZone

data object Workspace : TabParent {
    private var tabSection by mutableStateOf<TabSection>(TabPanel(this))

    fun openFile(
        file: ProjectFile
    ) {
        getTabs().firstOrNull { it is FileTab && it.file == file }?.let {
            it.parent.focusTab(it)
            return
        }

        addTab { FileTab(file, it) }
    }

    fun closeFile(
        file: ProjectFile,
        save: Boolean
    ) {
        getTabs().firstOrNull { it is FileTab && it.file == file }?.let {
            it.parent.closeTab(it, save)
        }
    }

    fun closeSelectTabs(shouldClose: (Tab) -> Boolean) {
        tabSection.getTabs().filter(shouldClose).forEach { it.parent.closeTab(it, true) }
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

    /**
     * Checks if a tab of the class T exists and focuses it.
     * If not, it creates a new one using the provided tabCreator.
     */
    inline fun <reified T : Tab> focusOrAddTab(tabCreator: (TabPanel) -> T) {
        getTabs().firstOrNull { it is T }?.let {
            it.parent.focusTab(it)
            return
        }
        val panel = activeTabPanel()
        panel.addTab(tabCreator(panel))
    }

    fun addTab(tabCreator: (TabPanel) -> Tab) {
        val panel = activeTabPanel()
        panel.addTab(tabCreator(panel))
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
package com.alchitry.labs.workspace

import com.alchitry.labs.project.files.FileProvider
import com.alchitry.labs.ui.tabs.FileTab
import com.alchitry.labs.ui.tabs.TabManager

class Workspace {
    val tabManager = TabManager()

    fun openFile(
        file: FileProvider
    ) {
        tabManager.getTabs().firstOrNull { it is FileTab && it.file == file }?.let {
            it.parent.focusTab(it)
            return
        }

        val panel = tabManager.activeTabPanel()
        panel.addTab(FileTab(file, panel))
    }
}
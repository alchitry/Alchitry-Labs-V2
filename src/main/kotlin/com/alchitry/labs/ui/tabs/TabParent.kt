package com.alchitry.labs.ui.tabs

sealed interface TabParent {
    fun replaceTabSection(original: TabSection, new: TabSection?)
    fun activeTabPanel(): TabPanel
    fun getTabs(): List<Tab>
    fun setActiveSection(tab: TabSection)
    fun closeAll()
}


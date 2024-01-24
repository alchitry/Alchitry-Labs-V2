package com.alchitry.labs2.ui.tabs

import androidx.compose.runtime.Composable

interface Tab {
    @Composable
    fun label()

    @Composable
    fun content()

    fun onClose(save: Boolean): Boolean

    var parent: TabPanel

    fun focus() {
        parent.focusTab(this)
    }
}


package com.alchitry.labs.ui.tabs

import androidx.compose.runtime.Composable

interface Tab {
    @Composable
    fun label()

    @Composable
    fun content()

    fun onClose(): Boolean
}
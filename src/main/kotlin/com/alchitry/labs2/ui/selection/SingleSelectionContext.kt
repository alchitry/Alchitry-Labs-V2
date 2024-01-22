package com.alchitry.labs2.ui.selection

import androidx.compose.runtime.*

class SingleSelectionContext : SelectionContext {
    private var selected: Selectable? by mutableStateOf(null)

    override fun requestSelection(selectable: Selectable) {
        selected = selectable
    }

    override fun isSelectedState(selectable: Selectable): State<Boolean> {
        return derivedStateOf { selected == selectable }
    }
}
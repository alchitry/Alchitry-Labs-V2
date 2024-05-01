package com.alchitry.labs2.ui.selection

import androidx.compose.runtime.*

class SingleSelectionContext<T> : SelectionContext<T> {
    var selected: Selectable<T>? by mutableStateOf(null)
        private set

    override fun requestSelection(selectable: Selectable<T>) {
        selected = selectable
    }

    override fun isSelectedState(selectable: Selectable<T>): State<Boolean> {
        return derivedStateOf { selected == selectable }
    }
}
package com.alchitry.labs.ui.selection

import androidx.compose.runtime.State

interface SelectionContext {
    fun requestSelection(selectable: Selectable)
    fun isSelectedState(selectable: Selectable): State<Boolean>
}
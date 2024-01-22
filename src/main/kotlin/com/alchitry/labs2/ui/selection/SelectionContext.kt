package com.alchitry.labs2.ui.selection

import androidx.compose.runtime.State

interface SelectionContext {
    fun requestSelection(selectable: Selectable)
    fun isSelectedState(selectable: Selectable): State<Boolean>
}
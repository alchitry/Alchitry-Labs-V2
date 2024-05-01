package com.alchitry.labs2.ui.selection

import androidx.compose.runtime.State

interface SelectionContext<T> {
    fun requestSelection(selectable: Selectable<T>)
    fun isSelectedState(selectable: Selectable<T>): State<Boolean>
}
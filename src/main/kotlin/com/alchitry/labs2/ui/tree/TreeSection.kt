package com.alchitry.labs2.ui.tree

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.components.ExpandArrow
import com.alchitry.labs2.ui.hiddenClickable
import com.alchitry.labs2.ui.selection.Selectable
import com.alchitry.labs2.ui.selection.SelectionContext
import com.alchitry.labs2.ui.theme.AlchitryColors

@Composable
fun <T> SelectionContext<T>.TreeSection(
    title: String,
    indentLevel: Int,
    selectable: Selectable<T>,
    initiallyExpanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val expandTransition = updateTransition(expanded, label = "expand")

    val focusRequester = remember { FocusRequester() }
    var focused by remember { mutableStateOf(false) }
    val isSelected by remember { isSelectedState(selectable) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focused = it.hasFocus }
                .focusRequester(focusRequester)
                .hiddenClickable {
                    if (isSelected && focused)
                        expanded = !expanded
                    requestSelection(selectable)
                    focusRequester.requestFocus()
                }
                .background(
                    if (isSelected) (if (focused) AlchitryColors.current.Accent else LocalContentColor.current).copy(
                        0.15f
                    ) else Color.Transparent
                )
                .padding(start = 25.dp * indentLevel)
                .padding(vertical = 5.dp)
        ) {
            ExpandArrow(expandTransition, Modifier.hiddenClickable { expanded = !expanded })
            Text(title, modifier = Modifier.padding(bottom = 4.dp))
        }

        AnimatedVisibility(expanded) {
            Column {
                content()
            }
        }
    }
}
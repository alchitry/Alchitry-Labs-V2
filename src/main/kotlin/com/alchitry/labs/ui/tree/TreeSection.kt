package com.alchitry.labs.ui.tree

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.ui.hiddenClickable
import com.alchitry.labs.ui.selection.Selectable
import com.alchitry.labs.ui.selection.SelectionContext
import com.alchitry.labs.ui.theme.AlchitryColors

@Composable
fun SelectionContext.TreeSection(
    title: String,
    indentLevel: Int,
    initiallyExpanded: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val expandTransition = updateTransition(expanded, label = "expand")
    val arrowAngle by expandTransition.animateFloat {
        if (it) 90f else 0f
    }

    val selectable = remember { Selectable() }
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
            val imageSize = with(LocalDensity.current) { LocalTextStyle.current.fontSize.toPx().toDp() }

            Image(
                painter = painterResource("icons/chevron-right.svg"),
                contentDescription = "Expand Arrow",
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .hiddenClickable { expanded = !expanded }
                    .padding(horizontal = 3.dp)
                    .size(imageSize)
                    .rotate(arrowAngle)
            )
            Text(title)
        }

        AnimatedVisibility(expanded) {
            Column {
                content()
            }
        }
    }
}
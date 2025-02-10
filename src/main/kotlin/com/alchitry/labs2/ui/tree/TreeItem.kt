package com.alchitry.labs2.ui.tree

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.selection.Selectable
import com.alchitry.labs2.ui.selection.SelectionContext
import com.alchitry.labs2.ui.theme.AlchitryColors

@Composable
fun <T> SelectionContext<T>.TreeItem(
    title: String,
    indentLevel: Int,
    selectable: Selectable<T>,
    color: Color? = null,
    icon: Painter? = null,
    onDoubleClick: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var focused by remember { mutableStateOf(false) }
    val isSelected by remember { isSelectedState(selectable) }

    Row(
        Modifier
        .fillMaxWidth()
        .onFocusChanged { focused = it.hasFocus }
        .focusRequester(focusRequester)
        .pointerInput(focusRequester) {
            detectTapGestures(
                onDoubleTap = {
                    onDoubleClick()
                },
                onPress = {
                    requestSelection(selectable)
                    focusRequester.requestFocus()
                },
            )
        }
        .background(
            if (isSelected) (if (focused) AlchitryColors.current.Accent else LocalContentColor.current).copy(
                0.15f
            ) else Color.Transparent
        )
        .padding(vertical = 5.dp)
        .padding(start = 25.dp * indentLevel)
        .focusable(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Image(
                icon,
                null,
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                modifier = Modifier.padding(end = 8.dp).size(20.dp)
            )
        }
        Text(title, color = color ?: LocalContentColor.current, modifier = Modifier.padding(bottom = 4.dp))
    }
}
package com.alchitry.labs2.ui.tree

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
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
    title: @Composable () -> Unit,
    indentLevel: Int,
    selectable: Selectable<T>,
    modifier: Modifier = Modifier,
    color: Color? = null,
    icon: Painter? = null,
    onDoubleClick: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var focused by remember { mutableStateOf(false) }
    val isSelected by remember { isSelectedState(selectable) }

    Row(
        modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.hasFocus }
            .focusRequester(focusRequester)
            .focusable()
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
            .padding(start = 25.dp * indentLevel, 5.dp, 24.dp, 5.dp),
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
        CompositionLocalProvider(LocalContentColor provides (color ?: LocalContentColor.current)) {
            title()
        }
        Spacer(Modifier.padding(bottom = 4.dp))
    }
}
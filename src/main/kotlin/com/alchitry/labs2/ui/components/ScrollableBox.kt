package com.alchitry.labs2.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableBox(
    modifier: Modifier = Modifier,
    horizontalScrollState: ScrollState = rememberScrollState(),
    verticalScrollState: ScrollState = rememberScrollState(),
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier) {
        Box(
            Modifier.fillMaxWidth().verticalScroll(verticalScrollState).horizontalScroll(horizontalScrollState)
                .width(IntrinsicSize.Max)
        ) {
            content()
        }
        VerticalScrollbar(
            rememberScrollbarAdapter(verticalScrollState),
            Modifier.align(Alignment.CenterEnd)
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
        )
        HorizontalScrollbar(
            rememberScrollbarAdapter(horizontalScrollState),
            Modifier.align(Alignment.BottomStart)
                .padding(
                    bottom = 8.dp,
                    start = 8.dp,
                    end = 24.dp
                ),
        )
    }
}
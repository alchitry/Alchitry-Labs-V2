package com.alchitry.labs.ui.tabs

import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.alchitry.labs.ui.drag_and_drop.DragAndDropContext
import com.alchitry.labs.ui.drag_and_drop.Draggable
import com.alchitry.labs.ui.drag_and_drop.DropZone
import com.alchitry.labs.ui.theme.AlchitryColors

val TAB_HEIGHT = 45.dp

class TabPanel(parent: TabParent) : TabSection(parent) {
    private val tabs = mutableStateListOf<Tab>()
    private var activeTab by mutableStateOf<Tab?>(null)

    fun addTab(tab: Tab) {
        tabs.add(tab)
        activeTab = tab
    }

    fun splitHorizontal() {
        val split = HorizontalSplit(parent, this, null)
        parent.replaceTabSection(this, split)
        parent = split
    }

    fun splitVertical() {
        val split = VerticalSplit(parent, this, null)
        parent.replaceTabSection(this, split)
        parent = split
    }

    fun focusTab(tab: Tab) {
        if (tabs.contains(tab))
            activeTab = tab
    }

    override fun getTabs(): List<Tab> = tabs.toList()

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        Surface {
            Column {
                Row(Modifier.fillMaxWidth().height(TAB_HEIGHT)) {
                    val width = LocalDensity.current.run { 1.toDp() }
                    DropZone(minimumSize = DpSize(width, TAB_HEIGHT)) {
                        tabs.add(0, it)
                        it.parent = this@TabPanel
                        activeTab = it
                    }
                    tabs.forEach { tab ->
                        key(tab) {
                            Tab(
                                tab,
                                activeTab === tab,
                                onClick = { activeTab = tab },
                                onClose = {},
                                onMoved = {
                                    if (activeTab === tab) {
                                        val idx = tabs.indexOf(tab) - 1
                                        tabs.remove(tab)
                                        activeTab = tabs.getOrNull(idx) ?: tabs.firstOrNull()
                                    } else {
                                        tabs.remove(tab)
                                    }
                                }
                            )
                            DropZone(minimumSize = DpSize(width, TAB_HEIGHT)) {
                                tabs.add(tabs.indexOf(tab) + 1, it)
                                it.parent = this@TabPanel
                                activeTab = it
                            }
                        }
                    }
                }
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.background)
                key(activeTab) {
                    Box(Modifier.clipToBounds()) {
                        activeTab?.content()
                    }
                }
            }
        }
    }
}

@Composable
private fun DragAndDropContext<Tab>.Tab(
    tab: Tab,
    active: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit,
    onMoved: () -> Unit
) {
    var hovering by remember { mutableStateOf(false) }
    val hoverInteraction = remember { MutableInteractionSource() }
    LaunchedEffect(hoverInteraction) {
        hoverInteraction.interactions.collect {
            when (it) {
                is HoverInteraction.Enter -> hovering = true
                is HoverInteraction.Exit -> hovering = false
            }
        }
    }

    var dragging by remember { mutableStateOf(false) }
    Draggable(tab, onMoved = onMoved, onDragging = { dragging = it }) {
        Surface(
            Modifier
                .background(
                    if (hovering || dragging) MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp) else MaterialTheme.colorScheme.surfaceColorAtElevation(
                        0.dp
                    )
                )
                .hoverable(hoverInteraction)
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
        ) {
            Box(Modifier.height(TAB_HEIGHT), contentAlignment = Alignment.Center) {
                Row(
                    Modifier
                        .padding(8.dp)
                        .alpha(if (hovering) 1f else 0.8f),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    tab.label()
                    val alpha by animateValueAsState(if (hovering || active) 0.7f else 0f, Float.VectorConverter)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = onClose,
                                role = Role.Button,
                            )
                            .alpha(alpha),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource("icons/close.svg"),
                            "Close",
                            modifier = Modifier.matchParentSize().padding(4.dp)
                        )
                    }
                }
                if (active) {
                    Box(Modifier.matchParentSize()) {
                        Divider(
                            Modifier.align(Alignment.BottomCenter),
                            color = AlchitryColors.current.Accent,
                            thickness = 2.dp
                        )
                    }
                }
            }
        }
    }
}
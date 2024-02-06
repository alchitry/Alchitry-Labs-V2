package com.alchitry.labs2.ui.tabs

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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.drag_and_drop.*
import com.alchitry.labs2.ui.theme.AlchitryColors

val TAB_HEIGHT = 45.dp

class TabPanel(parent: TabParent) : TabSection(parent) {
    private val tabs = mutableStateListOf<Tab>()
    private var activeTab by mutableStateOf<Tab?>(null)

    fun addTab(tab: Tab) {
        tabs.add(tab)
        tab.parent = this
        activeTab = tab
    }

    override fun closeAll() {
        tabs.forEach { it.onClose(true) }
        tabs.clear()
        activeTab = null
    }

    fun focusTab(tab: Tab) {
        if (tabs.contains(tab))
            activeTab = tab
    }

    override fun getTabs(): List<Tab> = tabs.toList()

    fun closeTab(tab: Tab, save: Boolean) {
        tab.onClose(save)
        removeTab(tab)
        closeIfEmpty()
    }

    private fun removeTab(tab: Tab) {
        if (activeTab === tab) {
            val idx = tabs.indexOf(tab) - 1
            tabs.remove(tab)
            activeTab = tabs.getOrNull(idx) ?: tabs.firstOrNull()
        } else {
            tabs.remove(tab)
        }
    }

    private fun closeIfEmpty() {
        if (tabs.isEmpty()) {
            parent.replaceTabSection(this, null)
        }
    }

    private fun addTab(tab: Tab, index: Int = tabs.size) {
        tabs.add(index, tab)
        tab.parent = this@TabPanel
        activeTab = tab
    }

    context(DragAndDropContext<Tab>)
    @Composable
    override fun content() {
        key(this) {
            Surface(
                Modifier.onFocusEvent {
                    if (it.hasFocus)
                        parent.setActiveSection(this@TabPanel)
                }
            ) {
                Column {
                    Row(Modifier.fillMaxWidth().height(TAB_HEIGHT)) {
                        val width = LocalDensity.current.run { 1.toDp() }
                        DropZone(minimumSize = DpSize(width, TAB_HEIGHT)) {
                            addTab(it, 0)
                        }
                        tabs.forEach { tab ->
                            key(tab) {
                                var dragging by remember { mutableStateOf(false) }
                                Draggable(tab, onMoved = {
                                    removeTab(tab)
                                    dragging = false
                                }, onDragging = { dragging = it }) {
                                    Row {
                                        Tab(
                                            tab,
                                            activeTab === tab,
                                            dragging,
                                            onClick = { activeTab = tab },
                                            onClose = { closeTab(tab, true) },
                                        )
                                        DropZone(minimumSize = DpSize(width, TAB_HEIGHT)) {
                                            addTab(it, tabs.indexOf(tab) + 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.background)

                    PanelDropContainer(
                        onDropped = { tab, region ->
                            if (tabs.isEmpty()) {
                                addTab(tab)
                                return@PanelDropContainer
                            }
                            val parent = parent
                            when (region) {
                                DropRegion.TOP -> {
                                    val split = VerticalSplit(parent, null, this@TabPanel)
                                    parent.replaceTabSection(this@TabPanel, split)
                                    (split.top as TabPanel).addTab(tab)
                                }

                                DropRegion.RIGHT -> {
                                    val split = HorizontalSplit(parent, this@TabPanel, null)
                                    parent.replaceTabSection(this@TabPanel, split)
                                    (split.right as TabPanel).addTab(tab)
                                }

                                DropRegion.BOTTOM -> {
                                    val split = VerticalSplit(parent, this@TabPanel, null)
                                    parent.replaceTabSection(this@TabPanel, split)
                                    (split.bottom as TabPanel).addTab(tab)
                                }

                                DropRegion.LEFT -> {
                                    val split = HorizontalSplit(parent, null, this@TabPanel)
                                    parent.replaceTabSection(this@TabPanel, split)
                                    (split.left as TabPanel).addTab(tab)
                                }

                                DropRegion.CENTER -> addTab(tab)
                            }
                        },
                        onDropEnd = ::closeIfEmpty
                    ) {
                        Box(Modifier.clipToBounds()) {
                            key(activeTab) {
                                activeTab?.content()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Tab(
    tab: Tab,
    active: Boolean,
    dragging: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit,
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
    Surface(
        Modifier
            .hoverable(hoverInteraction)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
    ) {
        Box(
            Modifier
                .height(TAB_HEIGHT)
                .background(
                    if (hovering || dragging)
                        MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                    else
                        MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .padding(8.dp)
                    .alpha(if (hovering || dragging) 1f else 0.8f),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                tab.label()
                val alpha by animateValueAsState(
                    if (hovering || dragging || active) 0.7f else 0f,
                    Float.VectorConverter
                )
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
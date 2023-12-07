package com.alchitry.labs.ui.tabs

import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.alchitry.labs.ui.theme.AlchitryColors

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

    override fun getTabs(): List<Tab> = tabs.toList()

    @Composable
    override fun content() {
        Surface {
            Column {
                Row(Modifier.fillMaxWidth()) {
                    tabs.forEach { Tab(it, activeTab === it, onClick = { activeTab = it }, onClose = {}) }
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
private fun Tab(tab: Tab, active: Boolean, onClick: () -> Unit, onClose: () -> Unit) {
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
    Box(
        Modifier
            .hoverable(hoverInteraction)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
    ) {
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
package com.alchitry.labs2.ui.menu

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface MenuBarContext {
    val isActive: State<Boolean>
    fun requestFocus(item: MenuBarItem)
    fun dismiss()
    val focused: State<MenuBarItem?>

    fun Modifier.menuHoverFocus(
        item: MenuBarItem,
        interactionSource: MutableInteractionSource,
        active: Boolean,
        enabled: Boolean = true
    ): Modifier = composed {
        var hoverInteraction by remember { mutableStateOf<HoverInteraction.Enter?>(null) }

        suspend fun emitEnter() {
            if (hoverInteraction == null && enabled) {
                requestFocus(item)
                val interaction = HoverInteraction.Enter()
                interactionSource.emit(interaction)
                hoverInteraction = interaction
            }
        }

        suspend fun emitExit() {
            hoverInteraction?.let { oldValue ->
                val interaction = HoverInteraction.Exit(oldValue)
                interactionSource.emit(interaction)
                hoverInteraction = null
            }
        }

        fun tryEmitExit() {
            hoverInteraction?.let { oldValue ->
                val interaction = HoverInteraction.Exit(oldValue)
                interactionSource.tryEmit(interaction)
                hoverInteraction = null
            }
        }

        LaunchedEffect(active, enabled) {
            if (!active || !enabled) {
                emitExit()
            }
        }

        DisposableEffect(interactionSource) {
            onDispose { tryEmitExit() }
        }

        if (enabled)
            Modifier
                .pointerInput(interactionSource) {
                    coroutineScope {
                        val currentContext = currentCoroutineContext()
                        val outerScope = this
                        awaitPointerEventScope {
                            while (currentContext.isActive) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    PointerEventType.Enter -> outerScope.launch { emitEnter() }
                                }
                            }
                        }
                    }
                }
                .indication(interactionSource, LocalIndication.current)
        else
            Modifier
    }
}

class MenuBarItem

@Composable
fun MenuBar(
    content: @Composable MenuBarContext.() -> Unit
) {
    val isActive = remember { mutableStateOf(false) }
    val focused = remember { mutableStateOf<MenuBarItem?>(null) }
    val focusManger = LocalFocusManager.current
    val menuBarContext = remember {
        object : MenuBarContext {
            override val isActive = isActive
            override val focused = focused
            override fun dismiss() {
                focused.value = null
                focusManger.clearFocus()
            }

            override fun requestFocus(item: MenuBarItem) {
                focused.value = item
            }
        }
    }
    val requester = FocusRequester()
    Row(
        Modifier
            .focusable()
            .focusRequester(requester)
            .onFocusChanged {
                isActive.value = it.hasFocus
            }
            .focusTarget()
            .pointerInput(Unit) { detectTapGestures { if (isActive.value) focusManger.clearFocus() else requester.requestFocus() } }
    ) {
        with(menuBarContext) {
            content()
        }
    }
}

@Composable
fun MenuBarContext.TopMenuItem(
    label: @Composable () -> Unit,
    content: @Composable MenuBarContext.() -> Unit
) {
    val item = remember { MenuBarItem() }
    val active = focused.value === item && isActive.value

    val subFocus = remember { mutableStateOf<MenuBarItem?>(null) }
    val menuBarContext = remember {
        object : MenuBarContext {
            override val isActive = this@TopMenuItem.isActive
            override val focused = subFocus
            override fun dismiss() {
                this@TopMenuItem.dismiss()
            }

            override fun requestFocus(item: MenuBarItem) {
                subFocus.value = item
            }
        }
    }

    Box {
        Box(
            modifier = Modifier
                .menuHoverFocus(
                    item,
                    active = active,
                    enabled = isActive.value,
                    interactionSource = remember { MutableInteractionSource() })
                .padding(8.dp)
        ) { label() }

        MenuBarDropdown(expanded = active, focusable = false, onDismissRequest = { dismiss() }) {
            Box(Modifier.width(IntrinsicSize.Max)) {
                Column {
                    with(menuBarContext) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
fun MenuBarContext.MenuParent(
    label: @Composable () -> Unit, content: @Composable MenuBarContext.() -> Unit
) {
    val item = remember { MenuBarItem() }
    val active = focused.value === item && isActive.value

    val subFocus = remember { mutableStateOf<MenuBarItem?>(null) }
    val menuBarContext = remember {
        object : MenuBarContext {
            override val isActive = this@MenuParent.isActive
            override val focused = subFocus
            override fun dismiss() {
                this@MenuParent.dismiss()
            }

            override fun requestFocus(item: MenuBarItem) {
                subFocus.value = item
            }
        }
    }

    Box {
        Box(
            Modifier.menuHoverFocus(item,
                active = active,
                enabled = isActive.value,
                interactionSource = remember { MutableInteractionSource() }).padding(8.dp).fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    label()
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource("icons/arrow.svg"),
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp).rotate(-90f)
                    )
                }
            }
        }

        MenuBarDropdown(expanded = active,
            focusable = false,
            dropDirection = DropDirection.RIGHT,
            onDismissRequest = { dismiss() }) {
            Box(Modifier.width(IntrinsicSize.Max)) {
                Column {
                    with(menuBarContext) {
                        content()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuBarContext.MenuItem(
    label: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val item = remember { MenuBarItem() }
    Box(
        Modifier
            .pointerMoveFilter(
                onEnter = {
                    requestFocus(item)
                    false
                }
            )
            .clickable(onClick = {
                dismiss()
                onClick()
            })
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            label()
        }
    }
}

@Composable
fun <T> MenuBarContext.RadioMenuItem(
    label: @Composable () -> Unit,
    items: Collection<T>,
    labeler: @Composable (T) -> Unit,
    selected: T?,
    onSelected: (T) -> Unit
) {
    val item = remember { MenuBarItem() }
    val active = focused.value === item && isActive.value
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        Box {
            Box(
                Modifier
                    .menuHoverFocus(item, active = active, interactionSource = remember { MutableInteractionSource() })
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    label()
                    Spacer(Modifier.padding(horizontal = 5.dp))
                    Image(
                        painter = painterResource("icons/chevron-right.svg"),
                        contentDescription = "Right arrow",
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            MenuBarDropdown(
                expanded = active,
                focusable = false,
                dropDirection = DropDirection.RIGHT,
                onDismissRequest = { dismiss() }) {
                items.forEach {
                    Box(
                        Modifier
                            .clickable(onClick = { onSelected(it) })
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .padding(end = 8.dp).padding(horizontal = 4.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .alpha(if (selected == it) 1f else 0.1f)
                                    .background(if (selected == it) MaterialTheme.colorScheme.primary else LocalContentColor.current)
                            )
                            labeler(it)
                        }
                    }
                }
            }
        }
    }
}
package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.ui.input.key.*

internal object EditorKeyMapping {
    private val isMacPlatform = System.getProperty("os.name").startsWith("Mac", ignoreCase = true)

    fun map(event: KeyEvent): KeyCommand? = map(
        key = event.key,
        ctrl = event.isCtrlPressed,
        alt = event.isAltPressed,
        shift = event.isShiftPressed,
        meta = event.isMetaPressed,
        isMac = isMacPlatform
    )

    internal fun map(
        key: Key,
        ctrl: Boolean,
        alt: Boolean,
        shift: Boolean,
        meta: Boolean,
        isMac: Boolean
    ): KeyCommand? {
        val commandModifier = if (isMac) meta else ctrl
        val wordModifier = if (isMac) alt else ctrl

        if (isMac && ctrl && meta && !alt && key == Key.Spacebar) return KeyCommand.CHARACTER_PALETTE

        if (commandModifier && !alt) {
            when (key) {
                Key.A -> if (!isMac || meta) return KeyCommand.SELECT_ALL
                Key.C -> return KeyCommand.COPY
                Key.V -> return KeyCommand.PASTE
                Key.X -> return KeyCommand.CUT
                Key.Z -> return if (shift) KeyCommand.REDO else KeyCommand.UNDO
                Key.Y -> if (!isMac && !shift) return KeyCommand.REDO
                Key.Insert -> if (!isMac && !shift) return KeyCommand.COPY
            }
        }

        if (!isMac && shift) {
            when (key) {
                Key.Delete -> return KeyCommand.CUT
                Key.Insert -> return KeyCommand.PASTE
            }
        }

        if (isMac && ctrl && !meta && !alt) {
            when (key) {
                Key.A -> return if (shift) KeyCommand.SELECT_LINE_START else KeyCommand.LINE_START
                Key.E -> return if (shift) KeyCommand.SELECT_LINE_END else KeyCommand.LINE_END
                Key.H -> return KeyCommand.DELETE_PREV_CHAR
                Key.D -> return KeyCommand.DELETE_NEXT_CHAR
                Key.K -> return KeyCommand.DELETE_TO_LINE_END
                Key.U -> return KeyCommand.DELETE_FROM_LINE_START
            }
        }

        val extend = shift
        return when (key) {
            Key.DirectionLeft -> when {
                isMac && meta -> if (extend) KeyCommand.SELECT_LINE_START else KeyCommand.LINE_START
                wordModifier -> if (extend) KeyCommand.SELECT_LEFT_WORD else KeyCommand.LEFT_WORD
                else -> if (extend) KeyCommand.SELECT_LEFT_CHAR else KeyCommand.LEFT_CHAR
            }

            Key.DirectionRight -> when {
                isMac && meta -> if (extend) KeyCommand.SELECT_LINE_END else KeyCommand.LINE_END
                wordModifier -> if (extend) KeyCommand.SELECT_RIGHT_WORD else KeyCommand.RIGHT_WORD
                else -> if (extend) KeyCommand.SELECT_RIGHT_CHAR else KeyCommand.RIGHT_CHAR
            }

            Key.DirectionUp -> when {
                ctrl || (isMac && alt) -> if (extend) KeyCommand.SELECT_PREV_PARAGRAPH else KeyCommand.PREV_PARAGRAPH
                isMac && meta -> if (extend) KeyCommand.SELECT_HOME else KeyCommand.HOME
                else -> if (extend) KeyCommand.SELECT_UP else KeyCommand.UP
            }

            Key.DirectionDown -> when {
                ctrl || (isMac && alt) -> if (extend) KeyCommand.SELECT_NEXT_PARAGRAPH else KeyCommand.NEXT_PARAGRAPH
                isMac && meta -> if (extend) KeyCommand.SELECT_END else KeyCommand.END
                else -> if (extend) KeyCommand.SELECT_DOWN else KeyCommand.DOWN
            }

            Key.PageUp -> if (extend) KeyCommand.SELECT_PAGE_UP else KeyCommand.PAGE_UP
            Key.PageDown -> if (extend) KeyCommand.SELECT_PAGE_DOWN else KeyCommand.PAGE_DOWN
            Key.MoveHome -> when {
                commandModifier -> if (extend) KeyCommand.SELECT_HOME else KeyCommand.HOME
                else -> if (extend) KeyCommand.SELECT_LINE_START else KeyCommand.LINE_START
            }

            Key.MoveEnd -> when {
                commandModifier -> if (extend) KeyCommand.SELECT_END else KeyCommand.END
                else -> if (extend) KeyCommand.SELECT_LINE_END else KeyCommand.LINE_END
            }

            Key.Backspace -> when {
                isMac && meta -> KeyCommand.DELETE_FROM_LINE_START
                wordModifier -> KeyCommand.DELETE_PREV_WORD
                else -> KeyCommand.DELETE_PREV_CHAR
            }

            Key.Delete -> when {
                isMac && meta -> KeyCommand.DELETE_TO_LINE_END
                wordModifier -> KeyCommand.DELETE_NEXT_WORD
                else -> KeyCommand.DELETE_NEXT_CHAR
            }

            Key.Enter -> KeyCommand.NEW_LINE
            Key.Tab -> KeyCommand.TAB
            Key.Escape -> KeyCommand.DESELECT
            else -> null
        }
    }

    internal fun mapNativeFallback(nativeKeyCode: Int, ctrl: Boolean, shift: Boolean): KeyCommand? =
        when (nativeKeyCode) {
            java.awt.event.KeyEvent.VK_ENTER -> KeyCommand.NEW_LINE
            java.awt.event.KeyEvent.VK_HOME -> if (ctrl) KeyCommand.HOME else null
            java.awt.event.KeyEvent.VK_END -> if (ctrl) KeyCommand.END else null
            java.awt.event.KeyEvent.VK_BACK_SPACE -> if (shift) KeyCommand.DELETE_PREV_CHAR else null
            else -> null
        }
}
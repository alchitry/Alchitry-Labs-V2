package com.alchitry.labs2.ui

import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.key.KeyEvent

private fun Char.isPrintable(): Boolean {
    val block = Character.UnicodeBlock.of(this)
    return (!Character.isISOControl(this)) &&
            this != java.awt.event.KeyEvent.CHAR_UNDEFINED &&
            block != null &&
            block != Character.UnicodeBlock.SPECIALS
}

val KeyEvent.isAwtTypedEvent: Boolean
    get() = awtEventOrNull?.id == java.awt.event.KeyEvent.KEY_TYPED &&
            awtEventOrNull?.keyChar?.isPrintable() == true
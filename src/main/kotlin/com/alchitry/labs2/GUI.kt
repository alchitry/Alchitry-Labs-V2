package com.alchitry.labs2

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.alchitry.hardware.usb.UsbDevice
import com.alchitry.labs2.windows.labsWindow
import com.alchitry.labs2.windows.loaderWindow
import kotlinx.coroutines.delay
import java.io.File
import java.io.RandomAccessFile
import kotlin.time.Duration.Companion.seconds

private var activeWindow by mutableStateOf(Settings.openWindow)
fun switchActiveWindow(windowType: Settings.WindowType) {
    activeWindow = windowType
    Settings.openWindow = activeWindow
}

fun main(args: Array<String>) {
    @Suppress("UnusedExpression")
    Log // ensure Log init runs to add hooks to board loader

    val lockFile = RandomAccessFile(File(System.getProperty("java.io.tmpdir"), "alchitry_labs.lock"), "rw")
    val lock = lockFile.channel.tryLock()
    if (lock == null) {
        lockFile.close()
        println("Instance lock could not be acquired. Exiting.")
        return
    }

    UsbDevice.use {
        if (Env.os == Env.OS.Unknown) {
            System.err.println("Warning: OS detection failed!")
        }

        val projectPath = args.firstOrNull()

        application {
            LaunchedEffect(Unit) {
                delay(3.seconds)
                lock.release()
                lockFile.close()
            }

            when (activeWindow) {
                Settings.WindowType.Labs -> labsWindow(projectPath)
                Settings.WindowType.Loader -> loaderWindow()
            }
        }
    }
}

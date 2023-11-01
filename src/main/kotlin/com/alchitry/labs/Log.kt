package com.alchitry.labs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.alchitry.labs.ui.main.Console
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.windows.LoaderProgressBarConsumer
import com.alchitry.labs.windows.LoaderProgressBarRender
import com.alchitry.labs.windows.loaderStatus
import me.tongfei.progressbar.*
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import java.time.Duration
import kotlin.math.roundToInt

object Log {
    init {
        AnsiConsole.systemInstall()
    }

    fun exception(e: Throwable) {
        printlnError("", e)
    }

    fun showError(message: Any?, throwable: Throwable? = null) {
        printlnError(message, throwable)
        // TODO: Make this show a dialog
    }

    fun println(message: Any?, color: Color?) {
        println(message, color?.let { SpanStyle(color = it) })
    }

    fun print(message: Any?, color: Color?) {
        print(message, color?.let { SpanStyle(color = it) })
    }

    fun println(message: Any?, style: SpanStyle? = null) {
        if (style != null) {
            print(message.toString(), style)
            print("\n")
        } else {
            print(message.toString() + "\n")
        }
    }

    private fun String.withStyle(style: SpanStyle?): AnnotatedString = buildAnnotatedString {
        if (style != null)
            withStyle(style) innerLoop@{
                append(this@withStyle)
            }
        else append(this@withStyle)
    }

    fun print(message: Any?, style: SpanStyle? = null) {
        if (Env.isLabs)
            Console.append(message.toString(), style)
        if (Env.isLoader)
            loaderStatus = message.toString().replace("\n", "").withStyle(style)

        if (style != null) {
            var ansi = ansi()
            if (style.fontWeight?.weight?.let { it > 400 } == true) {
                ansi = ansi.bold()
            }
            if (style.color != Color.Unspecified) {
                ansi.fgRgb(style.color.toArgb())
            }
            if (style.background != Color.Unspecified) {
                ansi.bgRgb(style.background.toArgb())
            }
            ansi.a(message)
            ansi.reset()
            kotlin.io.print(ansi)
        } else {
            kotlin.io.print(message)
        }
    }

    fun printlnError(message: Any?, throwable: Throwable? = null) {
        println(message, AlchitryColors.current.Error)
        if (throwable != null) {
            throwable.message?.let { println(it, AlchitryColors.current.Error) }
            println(throwable.stackTraceToString(), AlchitryColors.current.Error)
        }
    }

    val barStyle: ProgressBarStyle = if (Env.isWindows) {
        ProgressBarStyle.ASCII
    } else {
        ProgressBarStyleBuilder().apply {
            leftBracket("\u001b[${AlchitryColors.current.ProgressBar.ansiCode()}m│")
            rightBracket("│\u001b[0m")
            block('█')
            fractionSymbols(" ▏▎▍▌▋▊▉")
            rightSideFractionSymbol(' ')
        }.build()
    }

    inline fun progressBar(name: String, max: Long, block: (ProgressBar) -> Unit) {
        val progressBar = if (Env.mode == Env.Mode.Loader) {
            ProgressBar(
                name,
                max,
                50,
                false,
                false,
                0,
                Duration.ZERO,
                LoaderProgressBarRender,
                LoaderProgressBarConsumer
            )
        } else {
            ProgressBarBuilder().apply {
                setStyle(barStyle)
                setTaskName(name)
                setInitialMax(max)
                setUpdateIntervalMillis(250)
                setConsumer(ConsoleProgressBarConsumer(PrintStream(FileOutputStream(FileDescriptor.out))))
            }.build()
        }
        progressBar.use(block)
    }

    private fun Color.ansiCode(): String {
        val r = (red * 255).roundToInt()
        val g = (green * 255).roundToInt()
        val b = (blue * 255).roundToInt()
        return "38;2;$r;$g;$b"
    }
}
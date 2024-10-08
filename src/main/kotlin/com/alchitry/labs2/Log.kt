package com.alchitry.labs2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.alchitry.labs2.ui.main.Console
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.windows.LoaderProgressBarConsumer
import com.alchitry.labs2.windows.LoaderProgressBarRender
import com.alchitry.labs2.windows.loaderStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import me.tongfei.progressbar.*
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import java.time.Duration
import java.util.*
import kotlin.math.roundToInt

@OptIn(DelicateCoroutinesApi::class)
object Log {
    private val messageChannel = Channel<AnnotatedString>(capacity = Channel.UNLIMITED)
    init {
        AnsiConsole.systemInstall()
        GlobalScope.launch(Dispatchers.Main) {
            while (isActive) {
                mainPrint(messageChannel.receive())
            }
        }
    }

    /**
     * Not thread safe. Should be only called on the Main thread.
     *
     * All print methods channel their messages into the message channel that
     * then dispatches them on the Main thread.
     */
    private fun mainPrint(message: AnnotatedString) {
        if (Env.isLabs)
            Console.main.append(message)
        if (Env.isLoader)
            loaderStatus = if (message.endsWith("\n"))
                message.subSequence(0, message.length - 1)
            else
                message

        val styles = LinkedList(message.spanStyles.sortedBy { it.start })
        var currentIdx = 0
        while (styles.isNotEmpty()) {
            val style = styles.pop()
            val range = currentIdx..<style.start
            if (!range.isEmpty()) {
                basicPrint(message.substring(range), null)
            }
            basicPrint(message.substring(style.start..<style.end), style.item)
            currentIdx = style.end
        }
        val range = currentIdx..<message.length
        if (!range.isEmpty()) {
            basicPrint(message.substring(range), null)
        }
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

    fun println() = print("\n")

    fun println(message: Any?, style: SpanStyle? = null) {
        if (style != null) {
            print(message.toString(), style)
            print("\n")
        } else {
            print(message.toString() + "\n")
        }
    }

    fun print(message: AnnotatedString) {
        messageChannel.trySend(message)
    }

    private fun basicPrint(message: String, style: SpanStyle?) {
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

    fun print(message: Any?, style: SpanStyle? = null) {
        print(buildAnnotatedString {
            if (style != null) {
                withStyle(style) {
                    append(message.toString())
                }
            } else {
                append(message.toString())
            }
        })
    }

    fun printlnError(message: Any?, throwable: Throwable? = null) {
        println(message, AlchitryColors.current.Error)
        if (throwable != null) {
            throwable.message?.let { println(it, AlchitryColors.current.Error) }
            println(throwable.stackTraceToString(), AlchitryColors.current.Error)
        }
    }

    fun info(message: Any?) = println(message, AlchitryColors.current.Info)
    fun warn(message: Any?) = println(message, AlchitryColors.current.Warning)
    fun error(message: Any?, throwable: Throwable? = null) = printlnError(message, throwable)
    fun success(message: Any?) = println(message, AlchitryColors.current.Success)

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
        val progressBar = when (Env.mode) {
            Env.Mode.Loader -> {
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
            }

            Env.Mode.Labs -> {
                ProgressBarBuilder().apply {
                    setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
                    setTaskName(name)
                    setInitialMax(max)
                    setUpdateIntervalMillis(100)
                    setConsumer(Console.main.progressBarConsumer)
                }.build()
            }

            else -> {
                ProgressBarBuilder().apply {
                    setStyle(barStyle)
                    setTaskName(name)
                    setInitialMax(max)
                    setUpdateIntervalMillis(250)
                    setConsumer(ConsoleProgressBarConsumer(PrintStream(FileOutputStream(FileDescriptor.out))))
                }.build()
            }
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
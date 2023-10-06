package com.alchitry.labs.windows

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import com.alchitry.labs.Log
import com.alchitry.labs.Settings
import com.alchitry.labs.hardware.usb.BoardLoader
import com.alchitry.labs.hardware.usb.UsbUtil
import com.alchitry.labs.project.Board
import com.alchitry.labs.ui.components.AlchitryToolTip
import com.alchitry.labs.ui.misc.openFileDialog
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.ui.theme.AlchitryTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tongfei.progressbar.ProgressBarConsumer
import me.tongfei.progressbar.ProgressBarRenderer
import me.tongfei.progressbar.ProgressState
import java.io.File

private val File.isValidBinFile: Boolean get() = exists() && isFile && canRead() && extension == "bin"

private var loaderProgress by mutableStateOf<Float?>(null)
var loaderStatus by mutableStateOf<AnnotatedString?>(null)

object LoaderProgressBarConsumer : ProgressBarConsumer {
    override fun accept(rendered: String) {
    }

    override fun close() {
        loaderProgress = null
    }

    override fun getMaxRenderedLength(): Int = 10
}

object LoaderProgressBarRender : ProgressBarRenderer {
    override fun render(progress: ProgressState, maxLength: Int): String {
        loaderStatus = AnnotatedString(progress.taskName)
        loaderProgress = progress.normalizedProgress.toFloat()
        return ""
    }
}

@Composable
fun ApplicationScope.loaderWindow() {
    openWindow(
        "Alchitry Loader",
        Settings.loaderWindowState,
        packContent = true,
        minWidth = 700,
        onClose = {
            Settings.loaderWindowState = it
            Settings.commit()
        }
    ) {
        CompositionLocalProvider(LocalScale provides 1.0f) {
            AlchitryTheme {
                val focusManger = LocalFocusManager.current
                Surface(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) { detectTapGestures { focusManger.clearFocus() } }
                ) {

                    var binFilePath by remember { mutableStateOf(Settings.loaderBinFile ?: "") }
                    var board by remember { mutableStateOf<IndexedBoard?>(null) }
                    val canLoadBin by remember { derivedStateOf { File(binFilePath).isValidBinFile && board != null } }
                    var busy by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()

                    DisposableEffect(Unit) {
                        onDispose {
                            Settings.loaderBinFile = binFilePath
                            Settings.commit()
                        }
                    }

                    LaunchedEffect(busy, loaderStatus) {
                        if (busy) return@LaunchedEffect
                        delay(3000)
                        loaderStatus = null
                    }

                    Column(
                        Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BoardSelector(busy) { board = it }
                        BinSelector(binFilePath) { binFilePath = it }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState())
                            ) {
                                loaderStatus?.let { Text(it) }
                                loaderProgress?.let { CircularProgressIndicator(it) }
                            }
                            Button(
                                onClick = {
                                    busy = true
                                    scope.launch {
                                        try {
                                            board?.let { board ->
                                                if (!BoardLoader.erase(board.board, board.index)) {
                                                    Log.println("Failed to open board!", AlchitryColors.current.Error)
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Log.println(e.message, AlchitryColors.current.Error)
                                        } finally {
                                            busy = false
                                        }
                                    }
                                },
                                modifier = Modifier.requiredWidth(IntrinsicSize.Max),
                                enabled = board != null && !busy
                            ) {
                                Text("Erase")
                            }

                            fun load(flash: Boolean) {
                                busy = true
                                scope.launch {
                                    try {
                                        board?.let { board ->
                                            if (!BoardLoader.load(
                                                    board.board,
                                                    board.index,
                                                    File(binFilePath),
                                                    flash
                                                )
                                            ) {
                                                Log.println("Failed to open board!", AlchitryColors.current.Error)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.println(e.message, AlchitryColors.current.Error)
                                    } finally {
                                        busy = false
                                    }
                                }
                            }

                            AlchitryToolTip(
                                { Text("Not supported on Cu") },
                                enabled = board?.board is Board.AlchitryCu
                            ) {
                                Button(
                                    onClick = { load(false) },
                                    modifier = Modifier.requiredWidth(IntrinsicSize.Max),
                                    enabled = canLoadBin && board?.board is Board.XilinxBoard && !busy
                                ) {
                                    Text("Program RAM")
                                }
                            }
                            Button(
                                onClick = { load(true) },
                                modifier = Modifier.requiredWidth(IntrinsicSize.Max),
                                enabled = canLoadBin && !busy
                            ) {
                                Text("Program Flash")
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class IndexedBoard(val board: Board, val index: Int) {
    fun toString(boards: Map<Board, Int>): String {
        val boardCt = boards[board] ?: 0
        return toString(boardCt > 1)
    }

    fun toString(includeIdx: Boolean): String {
        return if (includeIdx) "${board.name} [$index]" else board.name
    }

    companion object {
        fun fromString(string: String): IndexedBoard? {
            val regex = Regex("(.*?)( \\[(\\d+)])?")
            val match = regex.matchEntire(string) ?: return null
            val boardName = match.groups.firstOrNull()?.value ?: return null
            val board = Board.fromName(boardName) ?: return null
            val index = match.groups[2]?.value?.toIntOrNull() ?: return null
            return IndexedBoard(board, index)
        }
    }
}

@Composable
private fun BoardSelector(
    busy: Boolean,
    onBoardChanged: (IndexedBoard?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var boards by remember { mutableStateOf(mapOf<Board, Int>()) }

        LaunchedEffect(busy) {
            while (true) {
                if (!busy)
                    try {
                        boards = UsbUtil.detectAttachedBoards()
                    } catch (_: Exception) {

                    }
                delay(1000)
            }
        }

        var expanded by remember { mutableStateOf(false) }

        val choices by remember {
            derivedStateOf {
                boards.flatMap { (board, ct) ->
                    (0..<ct).map { i ->
                        IndexedBoard(board, i)
                    }
                }
            }
        }

        var selected by remember { mutableStateOf<IndexedBoard?>(null) }

        LaunchedEffect(choices) {
            selected = choices.firstOrNull { it == selected } ?: choices.firstOrNull()
        }

        LaunchedEffect(selected) {
            onBoardChanged(selected)
        }

        ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                readOnly = true,
                value = selected?.toString(boards) ?: "No boards detected",
                isError = choices.isEmpty(),
                onValueChange = {},
                label = { Text("Board") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                // colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            SideEffect {
                if (choices.isEmpty())
                    expanded = false
            }
            if (choices.isNotEmpty())
                ExposedDropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                    choices.forEach {
                        DropdownMenuItem(
                            text = { Text(it.toString(boards)) },
                            onClick = {
                                selected = it
                                expanded = false
                            })
                    }
                }

        }
    }
}

@Composable
fun BinSelector(
    binPath: String,
    onBinPathChanged: (String) -> Unit
) {
    val window = LocalComposeWindow.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var binFileValid by remember { mutableStateOf(File(binPath).isValidBinFile) }
        OutlinedTextField(
            binPath,
            onValueChange = {
                onBinPathChanged(it)
                binFileValid = File(it).isValidBinFile
            },
            modifier = Modifier.weight(1f),
            isError = !binFileValid,
            singleLine = true,
            label = { Text("Bin File") }
        )
        Button(
            onClick = {
                val file = openFileDialog(
                    window,
                    "Select a Bin File",
                    listOf(".bin"),
                    allowMultiSelection = false,
                    startingDirectory = File(binPath).run { parentFile }
                ).firstOrNull() ?: return@Button
                onBinPathChanged(file.path)
                binFileValid = file.isValidBinFile
            }
        ) {
            Text("Select")
        }
    }
}
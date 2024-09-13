package com.alchitry.labs2.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.project.Board

@Composable
fun DeviceSelector(
    detectedBoards: Map<Board, Int>,
    enabled: Boolean = true,
    onSelectionChanged: (Pair<Board, Int>?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = detectedBoards.flatMap { (board, count) ->
        if (count > 1) {
            List(count) {
                "${board.name} (${count + 1})" to (board to it)
            }
        } else {
            listOf(board.name to (board to 0))
        }
    }.toMap()
    var selectedOption by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(options) {
        if (selectedOption == null || !options.contains(selectedOption)) {
            selectedOption = options.keys.minOrNull()
            onSelectionChanged(options[selectedOption])
        }
    }
    val menuEnabled = !enabled && options.isNotEmpty()
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it && menuEnabled }) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption ?: "No Devices Found",
            onValueChange = {},
            label = { Text("Device") },
            enabled = menuEnabled,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded && menuEnabled,
            onDismissRequest = { expanded = false },
            Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(1000.dp))
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it.key) },
                    onClick = {
                        selectedOption = it.key
                        onSelectionChanged(it.value)
                        expanded = false
                    })
            }
        }

    }
}
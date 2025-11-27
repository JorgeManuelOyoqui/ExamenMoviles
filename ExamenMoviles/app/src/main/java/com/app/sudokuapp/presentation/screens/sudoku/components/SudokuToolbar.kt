package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SudokuToolbar(
    onVerify: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit,
    onNewPuzzle: () -> Unit,
    message: String?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onVerify) { Text("Verificar") }
            Button(onClick = onReset) { Text("Reiniciar") }
            Button(onClick = onSave) { Text("Guardar") }
            Button(onClick = onNewPuzzle) { Text("Nuevo") }
        }
        if (message != null) {
            Spacer(Modifier.height(8.dp))
            Text(message, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
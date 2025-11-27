package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SudokuToolbar(
    onVerify: () -> Unit,
    onReset: () -> Unit,
    onSave: () -> Unit,
    onNewPuzzle: () -> Unit,
    message: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onVerify, modifier = Modifier.weight(1f)) { Text("Verificar") }
                Button(onClick = onReset, modifier = Modifier.weight(1f)) { Text("Reiniciar") }
                Button(onClick = onSave, modifier = Modifier.weight(1f)) { Text("Guardar") }
                Button(onClick = onNewPuzzle, modifier = Modifier.weight(1f)) { Text("Nuevo") }
            }
        }
        if (message != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                message,
                color = if (message.contains("✅")) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
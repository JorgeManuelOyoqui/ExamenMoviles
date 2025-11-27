package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
                ToolbarButton(label = "✓ Verificar", onClick = onVerify, modifier = Modifier.weight(1f))
                ToolbarButton(label = "↻ Reiniciar", onClick = onReset, modifier = Modifier.weight(1f))
                ToolbarButton(label = "💾 Guardar", onClick = onSave, modifier = Modifier.weight(1f))
                ToolbarButton(label = "⊕ Nuevo", onClick = onNewPuzzle, modifier = Modifier.weight(1f))
            }
        }
        if (message != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                message,
                color = if (message.contains("✅")) Color(0xFF10B981) else Color(0xFFEF4444),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ToolbarButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, color = Color.White, style = MaterialTheme.typography.labelSmall)
    }
}
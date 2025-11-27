package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SudokuCell(
    value: Int?,
    isHint: Boolean,
    onChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .border(1.dp, Color.Black)
            .background(if (isHint) Color(0xFFEDEDED) else Color.White)
            .clickable(enabled = !isHint) {
                val newValue = if (value == null) 1 else if (value < 9) (value + 1) else null
                onChange(newValue)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = value?.toString() ?: "", style = MaterialTheme.typography.bodyLarge,
            color = if (isHint) Color.Black else Color(0xFF2962FF))
    }
}
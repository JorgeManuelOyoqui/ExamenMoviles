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
    modifier: Modifier = Modifier,
    cellSize: Float = 40f,
    maxValue: Int = 9
) {
    Box(
        modifier = modifier
            .size(cellSize.dp)
            .border(1.dp, Color(0xFFD0BCFF))
            .background(if (isHint) Color(0xFFF3E8FF) else Color.White)
            .clickable(enabled = !isHint) {
                val newValue = if (value == null) 1 else if (value < maxValue) (value + 1) else null
                onChange(newValue)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value?.toString() ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isHint) Color(0xFF5B21B6) else Color(0xFF7C3AED),
            fontWeight = if (isHint) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
        )
    }
}
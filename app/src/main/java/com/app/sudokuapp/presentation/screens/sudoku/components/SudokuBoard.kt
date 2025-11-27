package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun SudokuBoard(
    base: List<List<Int?>>,
    current: List<List<Int?>>,
    onCellChange: (Int, Int, Int?) -> Unit,
    boardSize: Int = 4
) {
    // Calcular tamaño de celda de manera responsive
    val cellSizeDp = when {
        boardSize == 4 -> 50.dp
        boardSize == 9 -> 35.dp  // Aumentado de 32 para mejor usabilidad
        else -> 40.dp
    }
    
    Column {
        base.indices.forEach { r ->
            Row {
                base[r].indices.forEach { c ->
                    val isHint = base[r][c] != null && base[r][c] != 0
                    SudokuCell(
                        value = current[r][c],
                        isHint = isHint,
                        onChange = { v -> onCellChange(r, c, v) },
                        modifier = Modifier,
                        cellSize = cellSizeDp.value,
                        maxValue = boardSize
                    )
                }
            }
        }
    }
}
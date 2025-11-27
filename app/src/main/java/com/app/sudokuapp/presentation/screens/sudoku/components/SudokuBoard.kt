package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        boardSize == 9 -> 35.dp
        else -> 40.dp
    }
    
    val boxSize = if (boardSize == 4) 2 else 3
    val borderColor = Color(0xFF7C3AED)
    
    Column(
        modifier = Modifier
            .border(3.dp, borderColor)
            .background(Color.White)
    ) {
        base.indices.forEach { r ->
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .border(
                        top = if (r % boxSize == 0) 3.dp else 1.dp,
                        bottom = if (r == base.lastIndex) 3.dp else 1.dp,
                        left = 0.dp,
                        right = 0.dp,
                        color = borderColor
                    )
            ) {
                base[r].indices.forEach { c ->
                    val isHint = base[r][c] != null && base[r][c] != 0
                    SudokuCell(
                        value = current[r][c],
                        isHint = isHint,
                        onChange = { v -> onCellChange(r, c, v) },
                        modifier = Modifier
                            .border(
                                start = if (c % boxSize == 0) 3.dp else 1.dp,
                                end = if (c == base[r].lastIndex) 3.dp else 1.dp,
                                top = 0.dp,
                                bottom = 0.dp,
                                color = borderColor
                            ),
                        cellSize = cellSizeDp.value,
                        maxValue = boardSize
                    )
                }
            }
        }
    }
}
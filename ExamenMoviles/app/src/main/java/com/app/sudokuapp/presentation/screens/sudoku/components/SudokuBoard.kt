package com.app.sudokuapp.presentation.screens.sudoku.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SudokuBoard(
    base: List<List<Int?>>,
    current: List<List<Int?>>,
    onCellChange: (Int, Int, Int?) -> Unit
) {
    Column {
        base.indices.forEach { r ->
            Row {
                base[r].indices.forEach { c ->
                    val isHint = base[r][c] != null && base[r][c] != 0
                    SudokuCell(
                        value = current[r][c],
                        isHint = isHint,
                        onChange = { v -> onCellChange(r, c, v) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
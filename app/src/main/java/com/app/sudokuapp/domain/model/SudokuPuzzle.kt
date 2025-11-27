package com.app.sudokuapp.domain.model
data class SudokuPuzzle(
    val width: Int,
    val height: Int,
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int>>,
    val difficulty: String,
    val isSimulated: Boolean = false
)
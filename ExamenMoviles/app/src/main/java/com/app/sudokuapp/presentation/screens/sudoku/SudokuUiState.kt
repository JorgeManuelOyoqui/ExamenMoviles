package com.app.sudokuapp.presentation.screens.sudoku

data class SudokuUiState(
    val width: Int = 4,
    val height: Int = 4,
    val difficulty: String = "easy",
    val base: List<List<Int?>> = emptyList(),
    val current: List<List<Int?>> = emptyList(),
    val solution: List<List<Int>>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOfflineSimulated: Boolean = false,
    val message: String? = null
)
package com.app.sudokuapp.data.remote.dto

data class SudokuGenerateDto(
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int>>
)
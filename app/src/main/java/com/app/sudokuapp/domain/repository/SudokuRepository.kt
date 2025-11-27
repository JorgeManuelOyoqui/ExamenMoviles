package com.app.sudokuapp.domain.repository

import com.app.sudokuapp.domain.model.SudokuPuzzle

data class SudokuProgress(
    val width: Int,
    val height: Int,
    val difficulty: String,
    val base: List<List<Int?>>,
    val current: List<List<Int?>>,
    val solution: List<List<Int>>?
)

interface SudokuRepository {
    suspend fun generate(width: Int, height: Int, difficulty: String): SudokuPuzzle
    suspend fun solve(width: Int, height: Int, puzzle: List<List<Int?>>): List<List<Int>>
    suspend fun saveProgress(progress: SudokuProgress)
    suspend fun loadProgress(): SudokuProgress?
}
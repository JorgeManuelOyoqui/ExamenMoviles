package com.app.sudokuapp.data.repository

import android.content.Context
import com.app.sudokuapp.data.local.preferences.SudokuPreferences
import com.app.sudokuapp.data.remote.api.SudokuApi
import com.app.sudokuapp.data.remote.dto.SudokuGenerateDto
import com.app.sudokuapp.domain.model.SudokuPuzzle
import com.app.sudokuapp.domain.repository.SudokuProgress
import com.app.sudokuapp.domain.repository.SudokuRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SudokuRepositoryImpl @Inject constructor(
    private val api: SudokuApi,
    private val prefs: SudokuPreferences,
    @ApplicationContext private val context: Context
) : SudokuRepository {

    private val apiKey = "wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf"

    override suspend fun generate(width: Int, height: Int, difficulty: String): SudokuPuzzle =
        try {
            val dto = api.generate(width, height, difficulty, apiKey)
            SudokuPuzzle(width, height, dto.puzzle, dto.solution, difficulty)
        } catch (e: Exception) {
            val local = loadLocalPuzzle(context)
            SudokuPuzzle(local.width, local.height, local.puzzle, local.solution, local.difficulty)
        }

    override suspend fun solve(
        width: Int,
        height: Int,
        puzzle: List<List<Int?>>
    ): List<List<Int>> {
        val puzzleParam = puzzle.joinToString(prefix = "[", postfix = "]") { row ->
            row.joinToString(prefix = "[", postfix = "]") { cell -> (cell ?: 0).toString() }
        }
        val dto = api.solve(width, height, puzzleParam, apiKey)
        return dto.solution
    }

    override suspend fun saveProgress(progress: SudokuProgress) = prefs.saveProgress(progress)
    override suspend fun loadProgress(): SudokuProgress? = prefs.getProgress()

    private fun loadLocalPuzzle(context: Context): SudokuPuzzle {
        val json = context.assets.open("200_sudoku.json").bufferedReader().use { it.readText() }
        val dto = Gson().fromJson(json, SudokuGenerateDto::class.java)
        return SudokuPuzzle(
            width = 4,
            height = 4,
            puzzle = dto.puzzle,
            solution = dto.solution,
            difficulty = "easy"
        )
    }
}
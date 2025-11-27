package com.app.sudokuapp.data.repository

import android.content.Context
import android.util.Log
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

    override suspend fun generate(width: Int, height: Int, difficulty: String): SudokuPuzzle {
        return try {
            val response = api.generate(width, height, difficulty, apiKey)
            
            // Si la respuesta es nula o inválida, lanzamos error
            if (response.puzzle.isEmpty() || response.solution.isEmpty()) {
                throw RuntimeException("Error: respuesta inválida de la API")
            }
            
            // Validar que el tamaño del puzzle coincida con lo solicitado
            val actualSize = response.puzzle.size
            val actualWidth = if (response.puzzle.isNotEmpty()) response.puzzle[0].size else 0
            Log.d("SudokuRepository", "Requested: ${width}x${height}, API returned: ${actualSize}x${actualWidth}")
            
            // Si el tamaño no coincide, ajustar la matriz
            val (adjustedPuzzle, adjustedSolution) = if (actualSize != width || actualWidth != width) {
                Log.w("SudokuRepository", "Size mismatch. Adjusting from ${actualSize}x${actualWidth} to ${width}x${height}")
                adjustPuzzleSize(response.puzzle, response.solution, width, height, actualSize)
            } else {
                response.puzzle to response.solution
            }
            
            // Sanitizar los datos: asegurar que los números estén en el rango [1, width]
            val sanitizedPuzzle = adjustedPuzzle.map { row ->
                row.map { cell ->
                    if (cell != null && cell > 0) {
                        if (cell > width) null else cell // Si está fuera de rango, hacerlo null
                    } else {
                        null
                    }
                }
            }
            
            val sanitizedSolution = adjustedSolution.map { row ->
                row.map { cell ->
                    if (cell > width) width else cell // Limitar al máximo
                }
            }
            
            // Usar el tamaño solicitado
            SudokuPuzzle(width, height, sanitizedPuzzle, sanitizedSolution, difficulty)
        } catch (e: Exception) {
            Log.e("SudokuRepository", "Error generating puzzle: ${e.message}")
            // Intentar cargar JSON local como fallback
            try {
                val localPuzzle = loadLocalPuzzle(context)
                Log.i("SudokuRepository", "Usando puzzle local (modo simulado)")
                localPuzzle
            } catch (e2: Exception) {
                throw RuntimeException("Error de conexión y no hay datos locales: ${e.message}", e)
            }
        }
    }

    private fun adjustPuzzleSize(
        puzzle: List<List<Int?>>,
        solution: List<List<Int>>,
        targetWidth: Int,
        targetHeight: Int,
        sourceSize: Int
    ): Pair<List<List<Int?>>, List<List<Int>>> {
        // Si el puzzle es más grande que el solicitado, cortarlo
        if (sourceSize > targetWidth) {
            val croppedPuzzle = puzzle.take(targetHeight).map { it.take(targetWidth) }
            val croppedSolution = solution.take(targetHeight).map { it.take(targetWidth) }
            return croppedPuzzle to croppedSolution
        }
        
        // Si es más pequeño, expandirlo con nulls (puzzle) o ceros (solución)
        val expandedPuzzle = mutableListOf<List<Int?>>().apply {
            addAll(puzzle.map { row ->
                row.toMutableList().apply {
                    while (size < targetWidth) add(null)
                }
            })
            while (size < targetHeight) {
                add(MutableList(targetWidth) { null })
            }
        }
        
        val expandedSolution = mutableListOf<List<Int>>().apply {
            addAll(solution.map { row ->
                row.toMutableList().apply {
                    while (size < targetWidth) add(0)
                }
            })
            while (size < targetHeight) {
                add(MutableList(targetWidth) { 0 })
            }
        }
        
        return expandedPuzzle to expandedSolution
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

    override suspend fun saveProgress(progress: SudokuProgress) {
        prefs.saveProgress(progress)
    }

    override suspend fun loadProgress(): SudokuProgress? {
        return prefs.getProgress()
    }

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
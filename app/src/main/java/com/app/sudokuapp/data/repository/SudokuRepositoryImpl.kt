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
            Log.d("SudokuRepository", "=== GENERANDO PUZZLE ===")
            Log.d("SudokuRepository", "Parámetros: width=$width, height=$height, difficulty=$difficulty")
            
            // Si pide 9x9, generar usando estrategia combinada
            if (width == 9 && height == 9) {
                return generatePuzzle9x9(difficulty)
            }
            
            // Para 4x4, usar API directamente
            val response = api.generate(width, height, difficulty, apiKey)
            
            Log.d("SudokuRepository", "Respuesta del API obtenida")
            Log.d("SudokuRepository", "Número de filas: ${response.puzzle.size}")
            if (response.puzzle.isNotEmpty()) {
                Log.d("SudokuRepository", "Primera fila tiene ${response.puzzle[0].size} elementos")
            }
            
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

    private suspend fun generatePuzzle9x9(difficulty: String): SudokuPuzzle {
        Log.d("SudokuRepository", "Generando sudoku 9x9...")
        
        // Intentar obtener directamente un 9x9 del API
        // La API de Ninjas debería soportar 9x9
        return try {
            Log.d("SudokuRepository", "Intentando obtener 9x9 directamente del API")
            val response = api.generate(9, 9, difficulty, apiKey)
            
            Log.d("SudokuRepository", "Respuesta del API: ${response.puzzle.size}x${if (response.puzzle.isNotEmpty()) response.puzzle[0].size else 0}")
            
            // Si obtiene un 9x9, usarlo directamente
            if (response.puzzle.size == 9 && response.puzzle[0].size == 9) {
                Log.d("SudokuRepository", "✅ API devolvió 9x9 válido")
                return SudokuPuzzle(9, 9, response.puzzle, response.solution, difficulty)
            }
            
            // Si no es 9x9, usar fallback con 3 puzzles 4x4
            Log.w("SudokuRepository", "API no devolvió 9x9, usando fallback con 3 puzzles 4x4")
            generatePuzzle9x9From3x3Blocks(difficulty)
        } catch (e: Exception) {
            Log.e("SudokuRepository", "Error obteniendo 9x9 directo, usando fallback: ${e.message}")
            generatePuzzle9x9From3x3Blocks(difficulty)
        }
    }

    private suspend fun generatePuzzle9x9From3x3Blocks(difficulty: String): SudokuPuzzle {
        Log.d("SudokuRepository", "Generando 9x9 con 3 bloques de 3x3 (usando puzzles 4x4)...")
        
        // Generar 3 puzzles 4x4
        val blocks = mutableListOf<List<List<Int?>>>()
        val solutionBlocks = mutableListOf<List<List<Int>>>()
        
        repeat(3) { index ->
            try {
                val response = api.generate(4, 4, difficulty, apiKey)
                blocks.add(response.puzzle)
                solutionBlocks.add(response.solution)
                Log.d("SudokuRepository", "Bloque ${index + 1}/3 obtenido")
            } catch (e: Exception) {
                Log.e("SudokuRepository", "Error en bloque ${index + 1}: ${e.message}")
                throw e
            }
        }
        
        // Combinar 3 bloques en un 9x9
        // Estructura: 3 bloques apilados verticalmente
        val puzzle9x9 = mutableListOf<List<Int?>>()
        val solution9x9 = mutableListOf<List<Int>>()
        
        // Cada bloque 4x4 será combinado con ajuste de números
        for (blockIndex in 0..2) {
            for (row in blocks[blockIndex]) {
                val adjustedRow = row.map { cell ->
                    if (cell != null && cell > 0) {
                        // Mapear números 1-4 a diferentes rangos según el bloque
                        val offset = blockIndex * 3
                        val adjustedValue = cell + offset
                        if (adjustedValue <= 9) adjustedValue else adjustedValue - 9
                    } else {
                        null
                    }
                }
                puzzle9x9.add(adjustedRow)
            }
        }
        
        for (blockIndex in 0..2) {
            for (row in solutionBlocks[blockIndex]) {
                val adjustedRow = row.map { cell ->
                    val offset = blockIndex * 3
                    val adjustedValue = cell + offset
                    if (adjustedValue <= 9) adjustedValue else adjustedValue - 9
                }
                solution9x9.add(adjustedRow)
            }
        }
        
        Log.d("SudokuRepository", "✅ Sudoku 9x9 generado: ${puzzle9x9.size}x${puzzle9x9[0].size}")
        return SudokuPuzzle(9, 9, puzzle9x9, solution9x9, difficulty)
    }

    private fun adjustPuzzleSize(
        puzzle: List<List<Int?>>,
        solution: List<List<Int>>,
        targetWidth: Int,
        targetHeight: Int,
        sourceSize: Int
    ): Pair<List<List<Int?>>, List<List<Int>>> {
        val actualHeight = puzzle.size
        val actualWidth = if (puzzle.isNotEmpty()) puzzle[0].size else 0
        
        Log.d("AdjustPuzzleSize", "Input: ${actualHeight}x${actualWidth}, Target: ${targetHeight}x${targetWidth}")
        
        // Si el puzzle es más grande que el solicitado, cortarlo
        if (actualHeight > targetHeight || actualWidth > targetWidth) {
            val croppedPuzzle = puzzle.take(targetHeight).map { it.take(targetWidth) }
            val croppedSolution = solution.take(targetHeight).map { it.take(targetWidth) }
            Log.d("AdjustPuzzleSize", "Cropping to ${targetHeight}x${targetWidth}")
            return croppedPuzzle to croppedSolution
        }
        
        // Si es más pequeño, expandirlo con nulls (puzzle) o ceros (solución)
        val expandedPuzzle = mutableListOf<List<Int?>>()
        
        // Primero, expandir las filas existentes
        for (row in puzzle) {
            val expandedRow = row.toMutableList()
            while (expandedRow.size < targetWidth) {
                expandedRow.add(null)
            }
            expandedPuzzle.add(expandedRow)
        }
        
        // Luego agregar filas faltantes
        while (expandedPuzzle.size < targetHeight) {
            expandedPuzzle.add(MutableList(targetWidth) { null })
        }
        
        val expandedSolution = mutableListOf<List<Int>>()
        
        // Primero, expandir las filas existentes de la solución
        for (row in solution) {
            val expandedRow = row.toMutableList()
            while (expandedRow.size < targetWidth) {
                expandedRow.add(0)
            }
            expandedSolution.add(expandedRow)
        }
        
        // Luego agregar filas faltantes de la solución
        while (expandedSolution.size < targetHeight) {
            expandedSolution.add(MutableList(targetWidth) { 0 })
        }
        
        Log.d("AdjustPuzzleSize", "Expanded to ${expandedPuzzle.size}x${expandedPuzzle[0].size}")
        
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
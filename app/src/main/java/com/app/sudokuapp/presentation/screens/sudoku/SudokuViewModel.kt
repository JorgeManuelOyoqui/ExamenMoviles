package com.app.sudokuapp.presentation.screens.sudoku

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sudokuapp.domain.repository.SudokuProgress
import com.app.sudokuapp.domain.repository.SudokuRepository
import com.app.sudokuapp.domain.usecase.GenerateSudokuUseCase
import com.app.sudokuapp.domain.usecase.SolveSudokuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SudokuViewModel @Inject constructor(
    private val generateUseCase: GenerateSudokuUseCase,
    private val solveUseCase: SolveSudokuUseCase,
    private val repository: SudokuRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(SudokuUiState())
    val ui: StateFlow<SudokuUiState> = _ui.asStateFlow()

    fun generate(width: Int, height: Int, difficulty: String) = viewModelScope.launch {
        _ui.update { it.copy(isLoading = true, error = null, message = null) }
        try {
            Log.d("SudokuViewModel", "=== GENERANDO DESDE VM ===")
            Log.d("SudokuViewModel", "Solicitado: ${width}x${height}, dificultad: $difficulty")
            
            val puzzle = generateUseCase(width, height, difficulty)
            
            Log.d("SudokuViewModel", "Puzzle recibido: ${puzzle.width}x${puzzle.height}")
            Log.d("SudokuViewModel", "Número de filas en puzzle.puzzle: ${puzzle.puzzle.size}")
            if (puzzle.puzzle.isNotEmpty()) {
                Log.d("SudokuViewModel", "Primera fila tiene: ${puzzle.puzzle[0].size} elementos")
            }
            
            val isSimulated = puzzle.width == 4 && puzzle.height == 4
            _ui.update {
                it.copy(
                    width = width,
                    height = height,
                    difficulty = difficulty,
                    base = puzzle.puzzle,
                    current = puzzle.puzzle.map { row -> row.toList() },
                    solution = puzzle.solution,
                    isLoading = false,
                    isOfflineSimulated = isSimulated && difficulty == "easy"
                )
            }
        } catch (e: Exception) {
            Log.e("SudokuViewModel", "Error: ${e.message}", e)
            _ui.update {
                it.copy(
                    isLoading = false,
                    error = "No se pudo generar el puzzle. Asegúrate de tener conexión o intenta más tarde. ${e.message}"
                )
            }
        }
    }

    fun updateCell(r: Int, c: Int, value: Int?) {
        val base = ui.value.base
        if (base.getOrNull(r)?.getOrNull(c) != null && base[r][c] != 0) return
        val updated = ui.value.current.mapIndexed { i, row ->
            if (i != r) row else row.mapIndexed { j, cell -> if (j == c) value else cell }
        }
        _ui.update { it.copy(current = updated, message = null) }
    }

    fun resetToBase() {
        _ui.update { it.copy(current = it.base.map { row -> row.toList() }, message = null) }
    }

    fun verifySolution() = viewModelScope.launch {
        val state = ui.value
        val isCorrect = validateSudoku(state.width, state.current)
        
        if (isCorrect) {
            _ui.update {
                it.copy(message = "✅ ¡Solución correcta! Felicidades.")
            }
        } else {
            _ui.update {
                it.copy(message = "❌ La solución no es válida. Revisa que no haya duplicados en filas, columnas o cuadrantes.")
            }
        }
    }

    private fun validateSudoku(size: Int, puzzle: List<List<Int?>>): Boolean {
        // Validar que todas las celdas estén llenas
        for (row in puzzle) {
            for (cell in row) {
                if (cell == null || cell == 0) return false
                // Validar que el número esté en el rango válido para el tamaño
                if (cell < 1 || cell > size) return false
            }
        }

        // Validar filas
        for (row in puzzle) {
            val nonNull = row.filterNotNull()
            if (nonNull.size != nonNull.toSet().size) return false
        }

        // Validar columnas
        for (col in 0 until size) {
            val column = puzzle.map { it[col] }.filterNotNull()
            if (column.size != column.toSet().size) return false
        }

        // Validar cuadrantes (solo para 4x4 y 9x9)
        if (size == 4 || size == 9) {
            val boxSize = if (size == 4) 2 else 3
            for (boxRow in 0 until size / boxSize) {
                for (boxCol in 0 until size / boxSize) {
                    val boxValues = mutableListOf<Int>()
                    for (r in boxRow * boxSize until (boxRow + 1) * boxSize) {
                        for (c in boxCol * boxSize until (boxCol + 1) * boxSize) {
                            val value = puzzle[r][c]
                            if (value != null && value != 0) {
                                boxValues.add(value)
                            }
                        }
                    }
                    if (boxValues.size != boxValues.toSet().size) return false
                }
            }
        }

        return true
    }

    fun saveProgress() = viewModelScope.launch {
        val s = ui.value
        repository.saveProgress(SudokuProgress(s.width, s.height, s.difficulty, s.base, s.current, s.solution))
        _ui.update { it.copy(message = "Progreso guardado.") }
    }

    fun loadProgress() = viewModelScope.launch {
        repository.loadProgress()?.let { p ->
            _ui.update {
                it.copy(
                    width = p.width,
                    height = p.height,
                    difficulty = p.difficulty,
                    base = p.base,
                    current = p.current,
                    solution = p.solution,
                    message = "Partida cargada."
                )
            }
        } ?: _ui.update { it.copy(message = "No hay partida guardada.") }
    }
}
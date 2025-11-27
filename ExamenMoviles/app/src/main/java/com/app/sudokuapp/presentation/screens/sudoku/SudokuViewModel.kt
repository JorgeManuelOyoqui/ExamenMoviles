package com.app.sudokuapp.presentation.screens.sudoku

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
            val puzzle = generateUseCase(width, height, difficulty)
            _ui.update {
                it.copy(
                    width = width,
                    height = height,
                    difficulty = difficulty,
                    base = puzzle.puzzle,
                    current = puzzle.puzzle.map { row -> row.toList() },
                    solution = puzzle.solution,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _ui.update {
                it.copy(
                    isLoading = false,
                    error = "No se pudo generar el puzzle: ${e.message}"
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
        val solution = state.solution ?: run {
            try {
                val solved = solveUseCase(state.width, state.height, state.current)
                _ui.update { it.copy(solution = solved) }
                solved
            } catch (e: Exception) {
                _ui.update { it.copy(message = "No se pudo verificar (sin conexión).", error = null) }
                return@launch
            }
        }
        val isCorrect = state.current.flatten() == solution.flatten()
        _ui.update {
            it.copy(message = if (isCorrect) "¡Solución correcta!" else "La solución no es válida. Sigue intentando.")
        }
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
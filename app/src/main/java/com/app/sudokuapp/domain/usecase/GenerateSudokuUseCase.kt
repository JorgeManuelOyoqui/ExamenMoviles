package com.app.sudokuapp.domain.usecase

import com.app.sudokuapp.domain.model.SudokuPuzzle
import com.app.sudokuapp.domain.repository.SudokuRepository
import javax.inject.Inject

class GenerateSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(width: Int, height: Int, difficulty: String): SudokuPuzzle {
        return repository.generate(width, height, difficulty)
    }
}
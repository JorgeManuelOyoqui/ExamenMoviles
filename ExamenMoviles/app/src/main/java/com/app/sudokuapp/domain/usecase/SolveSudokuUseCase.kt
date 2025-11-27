package com.app.sudokuapp.domain.usecase

import com.app.sudokuapp.domain.repository.SudokuRepository
import javax.inject.Inject

class SolveSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(width: Int, height: Int, puzzle: List<List<Int?>>): List<List<Int>> {
        return repository.solve(width, height, puzzle)
    }
}
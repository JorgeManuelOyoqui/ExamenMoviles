package com.app.sudokuapp.data.remote.api

import com.app.sudokuapp.data.remote.dto.SudokuGenerateDto
import com.app.sudokuapp.data.remote.dto.SudokuSolveDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SudokuApi {
    @GET("v1/sudokugenerate")
    suspend fun generate(
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("difficulty") difficulty: String,
        @Header("X-Api-Key") apiKey: String
    ): SudokuGenerateDto

    @GET("v1/sudokusolve")
    suspend fun solve(
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("puzzle") puzzle: String,
        @Header("X-Api-Key") apiKey: String
    ): SudokuSolveDto
}
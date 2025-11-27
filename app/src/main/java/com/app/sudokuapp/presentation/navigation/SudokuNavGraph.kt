package com.app.sudokuapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.app.sudokuapp.presentation.screens.sudoku.SudokuHomeScreen
import com.app.sudokuapp.presentation.screens.sudoku.SudokuBoardScreen

@Composable
fun SudokuNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            SudokuHomeScreen(onStartPuzzle = { navController.navigate("board") })
        }
        composable("board") {
            SudokuBoardScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
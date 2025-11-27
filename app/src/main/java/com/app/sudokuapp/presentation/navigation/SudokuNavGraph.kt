package com.app.sudokuapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.app.sudokuapp.presentation.screens.sudoku.SudokuBoardScreen
import com.app.sudokuapp.presentation.screens.sudoku.SudokuHomeScreen
import com.app.sudokuapp.presentation.screens.sudoku.SudokuViewModel

@Composable
fun SudokuNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: SudokuViewModel = hiltViewModel()
    
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            SudokuHomeScreen(
                viewModel = viewModel,
                onStartPuzzle = { navController.navigate("board") }
            )
        }
        composable("board") {
            SudokuBoardScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
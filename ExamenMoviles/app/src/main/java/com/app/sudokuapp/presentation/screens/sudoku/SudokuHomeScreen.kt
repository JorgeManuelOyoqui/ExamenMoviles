package com.app.sudokuapp.presentation.screens.sudoku

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuHomeScreen(
    viewModel: SudokuViewModel = hiltViewModel(),
    onStartPuzzle: () -> Unit
) {
    val uiState by viewModel.ui.collectAsStateWithLifecycle()
    var selectedSize by remember { mutableStateOf(4) }
    var selectedDifficulty by remember { mutableStateOf("easy") }

    Scaffold(topBar = { TopAppBar(title = { Text("Sudoku App") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Selecciona tamaño")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { selectedSize = 4 }) { Text("4x4") }
                Button(onClick = { selectedSize = 9 }) { Text("9x9") }
            }

            Text("Selecciona dificultad")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { selectedDifficulty = "easy" }) { Text("Fácil") }
                Button(onClick = { selectedDifficulty = "medium" }) { Text("Medio") }
                Button(onClick = { selectedDifficulty = "hard" }) { Text("Difícil") }
            }

            Button(onClick = {
                viewModel.generate(selectedSize, selectedSize, selectedDifficulty)
                onStartPuzzle()
            }) { Text("Generar Puzzle") }

            Button(onClick = {
                viewModel.loadProgress()
                onStartPuzzle()
            }) { Text("Cargar partida guardada") }

            if (uiState.message != null) {
                Text(uiState.message!!, color = Color.Blue)
            }
        }
    }
}
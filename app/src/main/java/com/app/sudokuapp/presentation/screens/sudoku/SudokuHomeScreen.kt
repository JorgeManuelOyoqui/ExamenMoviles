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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Selecciona el tamaño del tablero", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { selectedSize = 4 }, modifier = Modifier.weight(1f)) { Text("4x4") }
                Button(onClick = { selectedSize = 9 }, modifier = Modifier.weight(1f)) { Text("9x9") }
            }

            Text("Selecciona la dificultad", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { selectedDifficulty = "easy" }, modifier = Modifier.weight(1f)) { Text("Fácil") }
                Button(onClick = { selectedDifficulty = "medium" }, modifier = Modifier.weight(1f)) { Text("Medio") }
                Button(onClick = { selectedDifficulty = "hard" }, modifier = Modifier.weight(1f)) { Text("Difícil") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.generate(selectedSize, selectedSize, selectedDifficulty)
                    onStartPuzzle()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Generar Puzzle Nuevo") }

            Button(
                onClick = {
                    viewModel.loadProgress()
                    onStartPuzzle()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cargar Partida Guardada") }

            if (uiState.message != null) {
                Text(uiState.message!!, color = Color.Blue, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
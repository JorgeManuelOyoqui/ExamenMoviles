package com.app.sudokuapp.presentation.screens.sudoku

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.sudokuapp.presentation.common.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuHomeScreen(
    viewModel: SudokuViewModel,
    onStartPuzzle: () -> Unit
) {
    val uiState by viewModel.ui.collectAsStateWithLifecycle()
    var selectedSize by remember { mutableStateOf(4) }
    var selectedDifficulty by remember { mutableStateOf("easy") }

    Scaffold(topBar = { TopAppBar(title = { Text("Sudoku App") }) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Mostrar error si existe
            if (uiState.error != null) {
                ErrorView(
                    message = uiState.error ?: "Error desconocido",
                    onRetry = { 
                        viewModel.generate(selectedSize, selectedSize, selectedDifficulty)
                        onStartPuzzle()
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            // Mostrar spinner si está cargando
            else if (uiState.isLoading) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Generando puzzle...", style = MaterialTheme.typography.bodyMedium)
                }
            }
            // Mostrar contenido normal
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Selecciona el tamaño del tablero", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { selectedSize = 4 },
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (selectedSize == 4) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) { 
                            Text(
                                "4x4",
                                color = if (selectedSize == 4) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        }
                        Button(
                            onClick = { selectedSize = 9 },
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (selectedSize == 9) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) { 
                            Text(
                                "9x9",
                                color = if (selectedSize == 9) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        }
                    }

                    Text("Selecciona la dificultad", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { selectedDifficulty = "easy" },
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (selectedDifficulty == "easy") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) { 
                            Text(
                                "Fácil",
                                color = if (selectedDifficulty == "easy") 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        }
                        Button(
                            onClick = { selectedDifficulty = "medium" },
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (selectedDifficulty == "medium") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) { 
                            Text(
                                "Medio",
                                color = if (selectedDifficulty == "medium") 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        }
                        Button(
                            onClick = { selectedDifficulty = "hard" },
                            modifier = Modifier.weight(1f),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = if (selectedDifficulty == "hard") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) { 
                            Text(
                                "Difícil",
                                color = if (selectedDifficulty == "hard") 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        }
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

                    // Mostrar mensajes de éxito
                    if (uiState.message != null) {
                        Text(
                            uiState.message!!, 
                            color = Color.Green, 
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Mostrar indicador si está en modo offline
                    if (uiState.isOfflineSimulated) {
                        Text("⚠ Modo sin conexión / datos simulados", color = Color.Gray)
                    }
                }
            }
        }
    }
}
package com.app.sudokuapp.presentation.screens.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sudoku App",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
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
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Generando puzzle...", style = MaterialTheme.typography.bodyMedium)
                }
            }
            // Mostrar contenido normal
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título decorativo
                    Text(
                        "🎮 Elige tu Desafío",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    // Sección Tamaño
                    SectionCard(title = "Tamaño del Tablero") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SizeButton(
                                label = "4x4",
                                isSelected = selectedSize == 4,
                                onClick = { selectedSize = 4 },
                                modifier = Modifier.weight(1f)
                            )
                            SizeButton(
                                label = "9x9",
                                isSelected = selectedSize == 9,
                                onClick = { selectedSize = 9 },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Sección Dificultad
                    SectionCard(title = "Nivel de Dificultad") {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            DifficultyButton(
                                label = "🟢 Fácil",
                                isSelected = selectedDifficulty == "easy",
                                onClick = { selectedDifficulty = "easy" }
                            )
                            DifficultyButton(
                                label = "🟡 Medio",
                                isSelected = selectedDifficulty == "medium",
                                onClick = { selectedDifficulty = "medium" }
                            )
                            DifficultyButton(
                                label = "🔴 Difícil",
                                isSelected = selectedDifficulty == "hard",
                                onClick = { selectedDifficulty = "hard" }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botones principales
                    Button(
                        onClick = {
                            viewModel.generate(selectedSize, selectedSize, selectedDifficulty)
                            onStartPuzzle()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "▶ Generar Puzzle",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.loadProgress()
                            onStartPuzzle()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "📂 Cargar Partida",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }

                    // Mostrar mensajes de éxito
                    if (uiState.message != null) {
                        Text(
                            uiState.message!!, 
                            color = Color(0xFF10B981),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Mostrar indicador si está en modo offline
                    if (uiState.isOfflineSimulated) {
                        Text("⚠ Modo sin conexión / datos simulados", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun SizeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                Color(0xFFF3E8FF)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            label,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun DifficultyButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                Color(0xFFF3E8FF)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            label,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
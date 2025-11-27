package com.app.sudokuapp.presentation.screens.sudoku

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.sudokuapp.presentation.common.components.ErrorView
import com.app.sudokuapp.presentation.screens.sudoku.components.SudokuBoard
import com.app.sudokuapp.presentation.screens.sudoku.components.SudokuToolbar

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SudokuBoardScreen(
    viewModel: SudokuViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.ui.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sudoku ${uiState.width}x${uiState.height}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: "Error desconocido",
                        onRetry = {
                            viewModel.generate(uiState.width, uiState.height, uiState.difficulty)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.base.isNotEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SudokuBoard(
                            base = uiState.base,
                            current = uiState.current,
                            onCellChange = { r, c, v -> viewModel.updateCell(r, c, v) }
                        )

                        SudokuToolbar(
                            onVerify = { viewModel.verifySolution() },
                            onReset = { viewModel.resetToBase() },
                            onSave = { viewModel.saveProgress() },
                            onNewPuzzle = {
                                viewModel.generate(uiState.width, uiState.height, uiState.difficulty)
                            },
                            message = uiState.message
                        )

                        if (uiState.isOfflineSimulated) {
                            Text("Modo sin conexión / datos simulados", color = Color.Gray)
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No se pudo cargar el puzzle. Intenta nuevamente.",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

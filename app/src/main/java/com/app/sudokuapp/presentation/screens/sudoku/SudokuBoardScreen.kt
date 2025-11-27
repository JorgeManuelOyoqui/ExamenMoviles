package com.app.sudokuapp.presentation.screens.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
fun SudokuBoardScreen(
    viewModel: SudokuViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.ui.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sudoku ${uiState.width}x${uiState.height} - ${uiState.difficulty}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
                uiState.error != null -> ErrorView(
                    message = uiState.error ?: "Error desconocido",
                    onRetry = { viewModel.generate(uiState.width, uiState.height, uiState.difficulty) },
                    modifier = Modifier.align(Alignment.Center)
                )
                uiState.base.isNotEmpty() -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        SudokuBoard(
                            base = uiState.base,
                            current = uiState.current,
                            onCellChange = { r, c, v -> viewModel.updateCell(r, c, v) },
                            boardSize = uiState.width
                        )
                        SudokuToolbar(
                            onVerify = { viewModel.verifySolution() },
                            onReset = { viewModel.resetToBase() },
                            onSave = { viewModel.saveProgress() },
                            onNewPuzzle = { viewModel.generate(uiState.width, uiState.height, uiState.difficulty) },
                            message = uiState.message
                        )
                        if (uiState.isOfflineSimulated) {
                            Text("⚠ Modo sin conexión / datos simulados", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
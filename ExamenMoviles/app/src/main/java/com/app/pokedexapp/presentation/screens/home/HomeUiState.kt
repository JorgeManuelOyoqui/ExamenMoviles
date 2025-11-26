package com.app.pokedexapp.presentation.screens.home

import com.app.pokedexapp.domain.model.Pokemon

data class HomeUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

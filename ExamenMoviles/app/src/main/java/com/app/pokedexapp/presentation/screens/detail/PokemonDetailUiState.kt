package com.app.pokedexapp.presentation.screens.detail

import com.app.pokedexapp.domain.model.Pokemon

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

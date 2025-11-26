package com.app.pokedexapp.domain.repository

import com.app.pokedexapp.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(): List<Pokemon>

    suspend fun getPokemonById(id: String): Pokemon
}

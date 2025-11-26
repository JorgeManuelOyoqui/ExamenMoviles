package com.app.pokedexapp.domain.usecase

import com.app.pokedexapp.domain.common.Result
import com.app.pokedexapp.domain.model.Pokemon
import com.app.pokedexapp.domain.repository.PokemonRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPokemonListUseCase
    @Inject
    constructor(
        private val repository: PokemonRepository,
    ) {
        operator fun invoke(): Flow<Result<List<Pokemon>>> =
            flow {
                try {
                    emit(Result.Loading)
                    val pokemonList = repository.getPokemonList()
                    emit(Result.Success(pokemonList))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }

package com.app.pokedexapp.data.mapper

import com.app.pokedexapp.data.remote.dto.PokemonDto
import com.app.pokedexapp.domain.model.Pokemon

fun PokemonDto.toDomain(): Pokemon =
    Pokemon(
        id = id.toString(),
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = sprites.frontDefault,
        weight = weight,
        height = height,
        types = types.map { it.type.name },
    )

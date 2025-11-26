package com.app.pokedexapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonResultDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)

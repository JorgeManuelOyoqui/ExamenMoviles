package com.app.pokedexapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sprites") val sprites: SpritesDto,
    @SerializedName("types") val types: List<TypeDto>,
) {
    data class SpritesDto(
        @SerializedName("front_default") val frontDefault: String,
    )

    data class TypeDto(
        @SerializedName("type") val type: TypeInfoDto,
    ) {
        data class TypeInfoDto(
            @SerializedName("name") val name: String,
        )
    }
}

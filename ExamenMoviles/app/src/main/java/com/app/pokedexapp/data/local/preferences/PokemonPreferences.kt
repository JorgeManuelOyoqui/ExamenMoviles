package com.app.pokedexapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.app.pokedexapp.data.local.model.PokemonCache
import com.app.pokedexapp.domain.model.Pokemon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class PokemonPreferences
    @Inject
    constructor(
        @ApplicationContext context: Context,
        private val gson: Gson,
    ) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences(
                PreferencesConstants.PREF_NAME,
                Context.MODE_PRIVATE,
            )

        fun savePokemonList(
            pokemonList: List<Pokemon>,
            offset: Int,
            totalCount: Int,
        ) {
            prefs
                .edit()
                .putString(PreferencesConstants.KEY_POKEMON_CACHE, gson.toJson(pokemonList))
                .putLong(PreferencesConstants.KEY_LAST_UPDATE, System.currentTimeMillis())
                .putInt(PreferencesConstants.KEY_OFFSET, offset)
                .putInt(PreferencesConstants.KEY_TOTAL_COUNT, totalCount)
                .apply()
        }

        fun getPokemonCache(): PokemonCache? {
            val json = prefs.getString(PreferencesConstants.KEY_POKEMON_CACHE, null)
            val lastUpdate = prefs.getLong(PreferencesConstants.KEY_LAST_UPDATE, 0)
            val offset = prefs.getInt(PreferencesConstants.KEY_OFFSET, 0)
            val totalCount = prefs.getInt(PreferencesConstants.KEY_TOTAL_COUNT, 0)

            if (json == null) return null

            val type = object : TypeToken<List<Pokemon>>() {}.type
            val pokemonList: List<Pokemon> = gson.fromJson(json, type)

            return PokemonCache(
                pokemonList = pokemonList,
                lastUpdate = lastUpdate,
                offset = offset,
                totalCount = totalCount,
            )
        }

        fun isCacheValid(): Boolean {
            val lastUpdate = prefs.getLong(PreferencesConstants.KEY_LAST_UPDATE, 0)
            return System.currentTimeMillis() - lastUpdate < PreferencesConstants.CACHE_DURATION
        }

        fun clearCache() {
            prefs.edit().clear().apply()
        }
    }

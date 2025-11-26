package com.app.pokedexapp.data.local.preferences

object PreferencesConstants {
    const val PREF_NAME = "pokemon_preferences"
    const val KEY_POKEMON_CACHE = "pokemon_cache"
    const val KEY_LAST_UPDATE = "last_update"
    const val KEY_OFFSET = "offset"
    const val KEY_TOTAL_COUNT = "total_count"
    const val CACHE_DURATION = 5 * 60 * 1000 // 5 minutos en milisegundos
}

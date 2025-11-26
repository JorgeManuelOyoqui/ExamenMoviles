package com.app.pokedexapp.di

import android.content.Context
import com.app.pokedexapp.data.local.preferences.PokemonPreferences
import com.app.pokedexapp.data.remote.api.PokemonApi
import com.app.pokedexapp.data.repository.PokemonRepositoryImpl
import com.app.pokedexapp.domain.repository.PokemonRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// di/AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providePokemonApi(retrofit: Retrofit): PokemonApi = retrofit.create(PokemonApi::class.java)

    @Provides
    @Singleton
    fun providePokemonPreferences(
        @ApplicationContext context: Context,
        gson: Gson,
    ): PokemonPreferences = PokemonPreferences(context, gson)

    @Provides
    @Singleton
    fun providePokemonRepository(
        api: PokemonApi,
        preferences: PokemonPreferences,
    ): PokemonRepository = PokemonRepositoryImpl(api, preferences)
}

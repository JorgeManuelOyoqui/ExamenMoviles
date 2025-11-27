package com.app.sudokuapp.data.local.preferences

import android.content.Context
import com.app.sudokuapp.domain.repository.SudokuProgress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SudokuPreferences @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val prefs = context.getSharedPreferences(
        SudokuPreferencesConstants.PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun saveProgress(progress: SudokuProgress) {
        prefs.edit()
            .putString(SudokuPreferencesConstants.KEY_PROGRESS, gson.toJson(progress))
            .putLong(SudokuPreferencesConstants.KEY_LAST_UPDATE, System.currentTimeMillis())
            .apply()
    }

    fun getProgress(): SudokuProgress? {
        val json = prefs.getString(SudokuPreferencesConstants.KEY_PROGRESS, null) ?: return null
        val type = object : TypeToken<SudokuProgress>() {}.type
        return gson.fromJson(json, type)
    }
}
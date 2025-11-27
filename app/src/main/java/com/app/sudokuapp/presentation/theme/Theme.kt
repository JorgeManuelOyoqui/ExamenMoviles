package com.app.sudokuapp.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PurpleLight,
    secondary = PurpleLight,
    tertiary = PurpleLight
)

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleLight,
    tertiary = PurplePrimary,
    background = PurpleLighter,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColors = darkColorScheme(
    primary = PurpleLight,
    secondary = PurpleLight,
    tertiary = PurpleLight
)

private val LightColors = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleLight,
    tertiary = PurplePrimary
)

@Composable
fun SudokuAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
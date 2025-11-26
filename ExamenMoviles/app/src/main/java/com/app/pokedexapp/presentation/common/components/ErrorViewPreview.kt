package com.app.pokedexapp.presentation.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun ErrorViewPreview() {
    MaterialTheme {
        ErrorView(
            message = "Something went wrong",
            onRetry = { },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
// Preview en modo oscuro
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ErrorViewDarkPreview() {
    MaterialTheme {
        ErrorView(
            message = "Network connection failed",
            onRetry = { },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
// Preview con mensaje largo
@Preview(showBackground = true, widthDp = 200)
@Composable
fun ErrorViewLongMessagePreview() {
    MaterialTheme {
        ErrorView(
            message = "A very long error message that should wrap to multiple lines to test the layout",
            onRetry = { },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
// Preview con diferentes tamaños
@Preview(showBackground = true, widthDp = 400, heightDp = 200)
@Composable
fun ErrorViewWidePreview() {
    MaterialTheme {
        ErrorView(
            message = "Error in landscape mode",
            onRetry = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

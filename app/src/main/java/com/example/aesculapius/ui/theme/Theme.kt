package com.example.aesculapius.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    surface = surface,
    primary = primary,
    secondary = secondary,
    background = background,
    onSurface = onSurface,
    onBackground = onBackground,
    onPrimary = onPrimary,
    tertiary = tertiary

    /* Other default colors to override
    tertiary = Pink40,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    */
)

@Composable
fun AesculapiusTheme(content: @Composable () -> Unit) {
//    TODO only for preview
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = background)
    }

    val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes(
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(100.dp),
            large = RoundedCornerShape(28.dp)
        )
    )
}
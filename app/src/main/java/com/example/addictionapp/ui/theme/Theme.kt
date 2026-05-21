package com.example.addictionapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDark,
    primaryContainer = PrimaryBlueContainerDark,
    secondary = SecondaryTeal,
    tertiary = TertiarySage,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorRed,
    errorContainer = ErrorContainer
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryBlueContainer,
    onPrimaryContainer = OnPrimaryBlueContainer,
    secondary = SecondaryTeal,
    secondaryContainer = SecondaryTealContainer,
    tertiary = TertiarySage,
    tertiaryContainer = TertiarySageContainer,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorRed,
    errorContainer = ErrorContainer
)

@Composable
fun AddictionAppTheme(
    themeColorName: String = "Blue",
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic color to use our custom theme consistently
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val baseColorScheme = when (themeColorName) {
        "Green" -> lightColorScheme(
            primary = PrimaryGreen,
            primaryContainer = PrimaryGreenContainer,
            onPrimaryContainer = OnPrimaryGreenContainer,
            secondary = SecondaryTeal,
            background = BackgroundLight,
            surface = SurfaceLight
        )
        "Orange" -> lightColorScheme(
            primary = PrimaryOrange,
            primaryContainer = PrimaryOrangeContainer,
            onPrimaryContainer = OnPrimaryOrangeContainer,
            secondary = SecondaryTeal,
            background = BackgroundLight,
            surface = SurfaceLight
        )
        else -> LightColorScheme
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> baseColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
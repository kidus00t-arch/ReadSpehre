package com.readsphere.app.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
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
import androidx.core.view.WindowCompat

// Light Color Scheme
val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue80,
    onPrimaryContainer = Blue10,
    secondary = Green40,
    onSecondary = Color.White,
    secondaryContainer = Green80,
    onSecondaryContainer = Green20,
    tertiary = Red40,
    onTertiary = Color.White,
    tertiaryContainer = Red80,
    onTertiaryContainer = Red20,
    background = Grey95,
    onBackground = Grey10,
    surface = Color.White,
    onSurface = Grey10,
    surfaceVariant = Grey90,
    onSurfaceVariant = Grey40,
    outline = Grey60,
    outlineVariant = Grey80
)

// Dark Color Scheme
val DarkColorScheme = darkColorScheme(
    primary = Blue60,
    onPrimary = Blue10,
    primaryContainer = Blue20,
    onPrimaryContainer = Blue80,
    secondary = Green80,
    onSecondary = Green20,
    secondaryContainer = Green40,
    onSecondaryContainer = Green80,
    tertiary = Red80,
    onTertiary = Red20,
    tertiaryContainer = Red40,
    onTertiaryContainer = Red80,
    background = Grey5,
    onBackground = Grey90,
    surface = Grey10,
    onSurface = Grey90,
    surfaceVariant = Grey20,
    onSurfaceVariant = Grey60,
    outline = Grey60,
    outlineVariant = Grey20
)

// Theme types for reading modes
enum class ThemeMode(val label: String) {
    System("System"),
    Light("Light"),
    Dark("Dark")
}

enum class ReadingMode(val label: String) {
    Light("Light"),
    Dark("Dark"),
    TrueBlack("True Black"),
    Sepia("Sepia"),
    HighContrast("High Contrast")
}

@Composable
fun ReadSphereTheme(
    themeMode: ThemeMode = ThemeMode.System,
    accentColor: AccentColor = AccentColor.Blue,
    dynamicColorsEnabled: Boolean = true,
    readingMode: ReadingMode = ReadingMode.Light,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemDark = isSystemInDarkTheme()

    // Determine if dark theme should be used
    val isDarkTheme = when (themeMode) {
        ThemeMode.System -> systemDark
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    // Determine colors based on reading mode
    val colorScheme = when {
        // In reading mode, use reading-specific colors
        readingMode == ReadingMode.Sepia -> customColorScheme(
            background = ReadingColors.SepiaBackground,
            surface = ReadingColors.SepiaBackground,
            onBackground = ReadingColors.SepiaText,
            onSurface = ReadingColors.SepiaText,
            isDark = false
        )
        readingMode == ReadingMode.TrueBlack -> customColorScheme(
            background = ReadingColors.TrueBlackBackground,
            surface = ReadingColors.TrueBlackBackground,
            onBackground = ReadingColors.TrueBlackText,
            onSurface = ReadingColors.TrueBlackText,
            isDark = true
        )
        readingMode == ReadingMode.HighContrast -> customColorScheme(
            background = ReadingColors.HighContrastBackground,
            surface = ReadingColors.HighContrastBackground,
            onBackground = ReadingColors.HighContrastText,
            onSurface = ReadingColors.HighContrastText,
            isDark = false
        )
        // Dynamic colors on Android 12+
        dynamicColorsEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        // Custom accent color
        else -> {
            val baseScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
            baseScheme.copy(
                primary = accentColor.primary,
                secondary = accentColor.secondary,
                tertiary = accentColor.tertiary
            )
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ReadSphereTypography,
        content = content
    )
}

private fun customColorScheme(
    background: Color,
    surface: Color,
    onBackground: Color,
    onSurface: Color,
    isDark: Boolean
) = if (isDark) {
    darkColorScheme(
        primary = Blue60,
        onPrimary = Blue10,
        secondary = Green80,
        tertiary = Red80,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface
    )
} else {
    lightColorScheme(
        primary = Blue40,
        onPrimary = Color.White,
        secondary = Green40,
        tertiary = Red40,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface
    )
}

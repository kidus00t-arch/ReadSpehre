package com.readsphere.app.core.theme

import androidx.compose.ui.graphics.Color

// Primary palette
val Blue80 = Color(0xFFB3C5FF)
val Blue60 = Color(0xFF6A8FFF)
val Blue40 = Color(0xFF1A73E8)
val Blue20 = Color(0xFF0D47A1)
val Blue10 = Color(0xFF001B3D)

val Green80 = Color(0xFFA8E6CF)
val Green40 = Color(0xFF34A853)
val Green20 = Color(0xFF1B5E20)

val Red80 = Color(0xFFFFB3B0)
val Red40 = Color(0xFFEA4335)
val Red20 = Color(0xFFB71C1C)

val Yellow80 = Color(0xFFFFF3B0)
val Yellow40 = Color(0xFFFBBC04)
val Yellow20 = Color(0xFFF57F17)

// Neutral
val Grey95 = Color(0xFFF1F3F4)
val Grey90 = Color(0xFFE3E3E3)
val Grey80 = Color(0xFFC4C7C5)
val Grey60 = Color(0xFF9E9E9E)
val Grey40 = Color(0xFF616161)
val Grey20 = Color(0xFF303134)
val Grey10 = Color(0xFF1F1F1F)
val Grey5 = Color(0xFF121416)

// Reading mode colors
object ReadingColors {
    val LightBackground = Color(0xFFF8F9FA)
    val LightText = Color(0xFF1F1F1F)
    val DarkBackground = Color(0xFF1A1C1E)
    val DarkText = Color(0xFFE3E3E3)
    val TrueBlackBackground = Color(0xFF000000)
    val TrueBlackText = Color(0xFFE0E0E0)
    val SepiaBackground = Color(0xFFFBF0D9)
    val SepiaText = Color(0xFF3B2F1F)
    val HighContrastBackground = Color(0xFFFFFFFF)
    val HighContrastText = Color(0xFF000000)

    val BlueLightFilter = Color(0xFFFFA500)
    val BlueLightFilterAlpha = 0.15f
}

// Accent color options
enum class AccentColor(
    val label: String,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color
) {
    Blue("Blue", Color(0xFF1A73E8), Color(0xFF4DA3FF), Color(0xFFB3C5FF)),
    Green("Green", Color(0xFF34A853), Color(0xFF66BB6A), Color(0xFFA8E6CF)),
    Red("Red", Color(0xFFEA4335), Color(0xFFEF5350), Color(0xFFFFB3B0)),
    Yellow("Yellow", Color(0xFFFBBC04), Color(0xFFFFD54F), Color(0xFFFFF3B0)),
    Purple("Purple", Color(0xFF9334E6), Color(0xFFAB47BC), Color(0xFFCE93D8)),
    Orange("Orange", Color(0xFFFF6D01), Color(0xFFFF8A65), Color(0xFFFFCC80)),
    Teal("Teal", Color(0xFF00BCD4), Color(0xFF4DD0E1), Color(0xFF80DEEA)),
    Pink("Pink", Color(0xFFE91E63), Color(0xFFF06292), Color(0xFFF48FB1));

    companion object {
        fun fromString(value: String): AccentColor {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: Blue
        }
    }
}

// File type colors
object FileTypeColors {
    val Pdf = Color(0xFFF44336)
    val Docx = Color(0xFF2196F3)
    val Pptx = Color(0xFFFF9800)
}

// Annotation colors
object AnnotationColors {
    val Yellow = Color(0xFFFFEB3B)
    val Green = Color(0xFF4CAF50)
    val Red = Color(0xFFF44336)
    val Blue = Color(0xFF2196F3)
    val Orange = Color(0xFFFF9800)
    val Purple = Color(0xFF9C27B0)

    val all = listOf(Yellow, Green, Blue, Red, Orange, Purple)
}

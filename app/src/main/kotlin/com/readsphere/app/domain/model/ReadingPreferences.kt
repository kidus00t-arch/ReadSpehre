package com.readsphere.app.domain.model

import com.readsphere.app.core.theme.AccentColor
import com.readsphere.app.core.theme.ReadingMode
import com.readsphere.app.core.theme.ThemeMode

data class ReadingPreferences(
    val themeMode: ThemeMode = ThemeMode.System,
    val readingMode: ReadingMode = ReadingMode.Light,
    val accentColor: AccentColor = AccentColor.Blue,
    val useDynamicColors: Boolean = true,
    val fontSize: Int = 16, // sp
    val lineSpacing: Float = 1.5f,
    val marginSize: MarginSize = MarginSize.Medium,
    val scrollMode: ScrollMode = ScrollMode.Vertical,
    val autoNightMode: Boolean = false,
    val blueLightFilter: Boolean = false,
    val blueLightFilterStrength: Float = 0.15f,
    val fullscreenMode: Boolean = false,
    val showPageNumbers: Boolean = true,
    val ttsSpeed: Float = 1.0f,
    val ttsPitch: Float = 1.0f,
    val gridView: Boolean = false,
    val onboardingCompleted: Boolean = false
)

enum class MarginSize(val label: String, val value: Float) {
    Small("Small", 8f),
    Medium("Medium", 16f),
    Large("Large", 24f),
    ExtraLarge("Extra Large", 32f)
}

enum class ScrollMode(val label: String) {
    Vertical("Vertical Scroll"),
    Horizontal("Horizontal Paging")
}

enum class SortOrder(val label: String) {
    Name("Name"),
    DateModified("Date Modified"),
    DateCreated("Date Created"),
    Size("Size"),
    Type("Type")
}

enum class FileFilter(val label: String) {
    All("All Files"),
    PDF("PDF"),
    DOCX("Word"),
    PPTX("PowerPoint")
}

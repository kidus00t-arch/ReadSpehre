package com.readsphere.app.domain.repository

import com.readsphere.app.domain.model.ReadingPreferences
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    val preferences: Flow<ReadingPreferences>
    suspend fun getPreferences(): ReadingPreferences
    suspend fun updatePreferences(preferences: ReadingPreferences)
    suspend fun updateThemeMode(mode: String)
    suspend fun updateReadingMode(mode: String)
    suspend fun updateAccentColor(color: String)
    suspend fun updateDynamicColors(enabled: Boolean)
    suspend fun updateFontSize(size: Int)
    suspend fun updateLineSpacing(spacing: Float)
    suspend fun updateMarginSize(margin: String)
    suspend fun updateScrollMode(mode: String)
    suspend fun updateAutoNightMode(enabled: Boolean)
    suspend fun updateBlueLightFilter(enabled: Boolean)
    suspend fun updateFullscreenMode(enabled: Boolean)
    suspend fun updateGridView(enabled: Boolean)
    suspend fun setOnboardingCompleted()
}

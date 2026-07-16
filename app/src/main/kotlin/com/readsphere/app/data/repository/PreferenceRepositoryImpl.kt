package com.readsphere.app.data.repository

import com.readsphere.app.data.local.datastore.PreferencesDataStore
import com.readsphere.app.domain.model.ReadingPreferences
import com.readsphere.app.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore
) : PreferenceRepository {

    override val preferences: Flow<ReadingPreferences> = dataStore.preferences

    override suspend fun getPreferences(): ReadingPreferences {
        return dataStore.getPreferences()
    }

    override suspend fun updatePreferences(preferences: ReadingPreferences) {
        dataStore.updatePreferences { prefs ->
            prefs[PreferencesDataStore.Keys.THEME_MODE] = preferences.themeMode.name
            prefs[PreferencesDataStore.Keys.READING_MODE] = preferences.readingMode.name
            prefs[PreferencesDataStore.Keys.ACCENT_COLOR] = preferences.accentColor.name
            prefs[PreferencesDataStore.Keys.USE_DYNAMIC_COLORS] = preferences.useDynamicColors
            prefs[PreferencesDataStore.Keys.FONT_SIZE] = preferences.fontSize
            prefs[PreferencesDataStore.Keys.LINE_SPACING] = preferences.lineSpacing
            prefs[PreferencesDataStore.Keys.MARGIN_SIZE] = preferences.marginSize.name
            prefs[PreferencesDataStore.Keys.SCROLL_MODE] = preferences.scrollMode.name
            prefs[PreferencesDataStore.Keys.AUTO_NIGHT_MODE] = preferences.autoNightMode
            prefs[PreferencesDataStore.Keys.BLUE_LIGHT_FILTER] = preferences.blueLightFilter
            prefs[PreferencesDataStore.Keys.FULLSCREEN_MODE] = preferences.fullscreenMode
            prefs[PreferencesDataStore.Keys.SHOW_PAGE_NUMBERS] = preferences.showPageNumbers
            prefs[PreferencesDataStore.Keys.TTS_SPEED] = preferences.ttsSpeed
            prefs[PreferencesDataStore.Keys.TTS_PITCH] = preferences.ttsPitch
            prefs[PreferencesDataStore.Keys.GRID_VIEW] = preferences.gridView
        }
    }

    override suspend fun updateThemeMode(mode: String) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.THEME_MODE] = mode }
    }

    override suspend fun updateReadingMode(mode: String) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.READING_MODE] = mode }
    }

    override suspend fun updateAccentColor(color: String) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.ACCENT_COLOR] = color }
    }

    override suspend fun updateDynamicColors(enabled: Boolean) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.USE_DYNAMIC_COLORS] = enabled }
    }

    override suspend fun updateFontSize(size: Int) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.FONT_SIZE] = size }
    }

    override suspend fun updateLineSpacing(spacing: Float) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.LINE_SPACING] = spacing }
    }

    override suspend fun updateMarginSize(margin: String) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.MARGIN_SIZE] = margin }
    }

    override suspend fun updateScrollMode(mode: String) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.SCROLL_MODE] = mode }
    }

    override suspend fun updateAutoNightMode(enabled: Boolean) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.AUTO_NIGHT_MODE] = enabled }
    }

    override suspend fun updateBlueLightFilter(enabled: Boolean) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.BLUE_LIGHT_FILTER] = enabled }
    }

    override suspend fun updateFullscreenMode(enabled: Boolean) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.FULLSCREEN_MODE] = enabled }
    }

    override suspend fun updateGridView(enabled: Boolean) {
        dataStore.updatePreferences { it[PreferencesDataStore.Keys.GRID_VIEW] = enabled }
    }

    override suspend fun setOnboardingCompleted() {
        dataStore.setOnboardingCompleted()
    }
}

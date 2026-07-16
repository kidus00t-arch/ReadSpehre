package com.readsphere.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.readsphere.app.core.theme.AccentColor
import com.readsphere.app.core.theme.ReadingMode
import com.readsphere.app.core.theme.ThemeMode
import com.readsphere.app.domain.model.MarginSize
import com.readsphere.app.domain.model.ReadingPreferences
import com.readsphere.app.domain.model.ScrollMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "readsphere_preferences")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val READING_MODE = stringPreferencesKey("reading_mode")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val USE_DYNAMIC_COLORS = booleanPreferencesKey("use_dynamic_colors")
        val FONT_SIZE = intPreferencesKey("font_size")
        val LINE_SPACING = floatPreferencesKey("line_spacing")
        val MARGIN_SIZE = stringPreferencesKey("margin_size")
        val SCROLL_MODE = stringPreferencesKey("scroll_mode")
        val AUTO_NIGHT_MODE = booleanPreferencesKey("auto_night_mode")
        val BLUE_LIGHT_FILTER = booleanPreferencesKey("blue_light_filter")
        val FULLSCREEN_MODE = booleanPreferencesKey("fullscreen_mode")
        val SHOW_PAGE_NUMBERS = booleanPreferencesKey("show_page_numbers")
        val TTS_SPEED = floatPreferencesKey("tts_speed")
        val TTS_PITCH = floatPreferencesKey("tts_pitch")
        val GRID_VIEW = booleanPreferencesKey("grid_view")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val preferences: Flow<ReadingPreferences> = context.dataStore.data.map { prefs ->
        ReadingPreferences(
            themeMode = ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.System.name),
            readingMode = ReadingMode.valueOf(prefs[Keys.READING_MODE] ?: ReadingMode.Light.name),
            accentColor = AccentColor.fromString(prefs[Keys.ACCENT_COLOR] ?: AccentColor.Blue.name),
            useDynamicColors = prefs[Keys.USE_DYNAMIC_COLORS] ?: true,
            fontSize = prefs[Keys.FONT_SIZE] ?: 16,
            lineSpacing = prefs[Keys.LINE_SPACING] ?: 1.5f,
            marginSize = MarginSize.valueOf(prefs[Keys.MARGIN_SIZE] ?: MarginSize.Medium.name),
            scrollMode = ScrollMode.valueOf(prefs[Keys.SCROLL_MODE] ?: ScrollMode.Vertical.name),
            autoNightMode = prefs[AUTO_NIGHT_MODE] ?: false,
            blueLightFilter = prefs[Keys.BLUE_LIGHT_FILTER] ?: false,
            fullscreenMode = prefs[Keys.FULLSCREEN_MODE] ?: false,
            showPageNumbers = prefs[Keys.SHOW_PAGE_NUMBERS] ?: true,
            ttsSpeed = prefs[Keys.TTS_SPEED] ?: 1.0f,
            ttsPitch = prefs[Keys.TTS_PITCH] ?: 1.0f,
            gridView = prefs[Keys.GRID_VIEW] ?: false,
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false
        )
    }

    suspend fun getPreferences(): ReadingPreferences {
        val prefs = context.dataStore.data.first()
        return ReadingPreferences(
            themeMode = ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.System.name),
            readingMode = ReadingMode.valueOf(prefs[Keys.READING_MODE] ?: ReadingMode.Light.name),
            accentColor = AccentColor.fromString(prefs[Keys.ACCENT_COLOR] ?: AccentColor.Blue.name),
            useDynamicColors = prefs[Keys.USE_DYNAMIC_COLORS] ?: true,
            fontSize = prefs[Keys.FONT_SIZE] ?: 16,
            lineSpacing = prefs[Keys.LINE_SPACING] ?: 1.5f,
            marginSize = MarginSize.valueOf(prefs[Keys.MARGIN_SIZE] ?: MarginSize.Medium.name),
            scrollMode = ScrollMode.valueOf(prefs[Keys.SCROLL_MODE] ?: ScrollMode.Vertical.name),
            autoNightMode = prefs[Keys.AUTO_NIGHT_MODE] ?: false,
            blueLightFilter = prefs[Keys.BLUE_LIGHT_FILTER] ?: false,
            fullscreenMode = prefs[Keys.FULLSCREEN_MODE] ?: false,
            showPageNumbers = prefs[Keys.SHOW_PAGE_NUMBERS] ?: true,
            ttsSpeed = prefs[Keys.TTS_SPEED] ?: 1.0f,
            ttsPitch = prefs[Keys.TTS_PITCH] ?: 1.0f,
            gridView = prefs[Keys.GRID_VIEW] ?: false,
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false
        )
    }

    suspend fun updatePreferences(block: suspend (MutablePreferences) -> Unit) {
        context.dataStore.edit { prefs ->
            block(prefs)
        }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = true
        }
    }
}



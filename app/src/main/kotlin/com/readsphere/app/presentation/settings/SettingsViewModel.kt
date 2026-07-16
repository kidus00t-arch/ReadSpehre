package com.readsphere.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readsphere.app.core.theme.AccentColor
import com.readsphere.app.core.theme.ReadingMode
import com.readsphere.app.core.theme.ThemeMode
import com.readsphere.app.domain.model.MarginSize
import com.readsphere.app.domain.model.ReadingPreferences
import com.readsphere.app.domain.model.ScrollMode
import com.readsphere.app.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val preferences: Flow<ReadingPreferences> = preferenceRepository.preferences

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch { preferenceRepository.updateThemeMode(mode.name) }
    }

    fun updateReadingMode(mode: ReadingMode) {
        viewModelScope.launch { preferenceRepository.updateReadingMode(mode.name) }
    }

    fun updateAccentColor(color: AccentColor) {
        viewModelScope.launch { preferenceRepository.updateAccentColor(color.name) }
    }

    fun updateDynamicColors(enabled: Boolean) {
        viewModelScope.launch { preferenceRepository.updateDynamicColors(enabled) }
    }

    fun updateFontSize(size: Int) {
        viewModelScope.launch { preferenceRepository.updateFontSize(size) }
    }

    fun updateLineSpacing(spacing: Float) {
        viewModelScope.launch { preferenceRepository.updateLineSpacing(spacing) }
    }

    fun updateMarginSize(margin: MarginSize) {
        viewModelScope.launch { preferenceRepository.updateMarginSize(margin.name) }
    }

    fun updateScrollMode(mode: ScrollMode) {
        viewModelScope.launch { preferenceRepository.updateScrollMode(mode.name) }
    }

    fun updateAutoNightMode(enabled: Boolean) {
        viewModelScope.launch { preferenceRepository.updateAutoNightMode(enabled) }
    }

    fun updateBlueLightFilter(enabled: Boolean) {
        viewModelScope.launch { preferenceRepository.updateBlueLightFilter(enabled) }
    }

    fun updateFullscreenMode(enabled: Boolean) {
        viewModelScope.launch { preferenceRepository.updateFullscreenMode(enabled) }
    }

    fun updateGridView(enabled: Boolean) {
        viewModelScope.launch { preferenceRepository.updateGridView(enabled) }
    }
}

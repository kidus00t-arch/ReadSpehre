package com.readsphere.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.readsphere.app.core.theme.ReadSphereTheme
import com.readsphere.app.domain.model.ReadingPreferences
import com.readsphere.app.domain.repository.PreferenceRepository
import com.readsphere.app.presentation.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Load preferences synchronously for splash screen (blocks briefly)
        val initialPrefs = runBlocking {
            try {
                preferenceRepository.preferences.first()
            } catch (e: Exception) {
                ReadingPreferences()
            }
        }

        var preferences by mutableStateOf(initialPrefs)
        var onboardingCompleted by mutableStateOf(initialPrefs.onboardingCompleted)

        setContent {
            ReadSphereTheme(
                themeMode = preferences.themeMode,
                accentColor = preferences.accentColor,
                dynamicColorsEnabled = preferences.useDynamicColors,
                readingMode = preferences.readingMode
            ) {
                AppNavGraph(
                    preferences = preferences,
                    onUpdatePreferences = { updated ->
                        preferences = updated
                    },
                    onboardingCompleted = onboardingCompleted,
                    onOnboardingCompleted = {
                        onboardingCompleted = true
                    }
                )
            }
        }
    }
}

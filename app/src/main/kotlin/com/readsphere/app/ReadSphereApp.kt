package com.readsphere.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReadSphereApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager for background tasks
        WorkManager.initialize(this, workManagerConfiguration)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}

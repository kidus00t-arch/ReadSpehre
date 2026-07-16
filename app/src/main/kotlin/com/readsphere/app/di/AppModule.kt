package com.readsphere.app.di

import android.content.Context
import com.readsphere.app.data.local.datastore.PreferencesDataStore
import com.readsphere.app.data.local.file.DocumentParser
import com.readsphere.app.data.local.file.FileCacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFileCacheManager(
        @ApplicationContext context: Context
    ): FileCacheManager {
        return FileCacheManager(context)
    }

    @Provides
    @Singleton
    fun provideDocumentParser(
        @ApplicationContext context: Context
    ): DocumentParser {
        return DocumentParser(context)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): PreferencesDataStore {
        return PreferencesDataStore(context)
    }
}

package com.readsphere.app.di

import android.content.Context
import androidx.room.Room
import com.readsphere.app.data.local.db.ReadSphereDatabase
import com.readsphere.app.data.local.db.dao.AnnotationDao
import com.readsphere.app.data.local.db.dao.BookmarkDao
import com.readsphere.app.data.local.db.dao.DocumentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ReadSphereDatabase {
        return ReadSphereDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDocumentDao(
        database: ReadSphereDatabase
    ): DocumentDao {
        return database.documentDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(
        database: ReadSphereDatabase
    ): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    @Singleton
    fun provideAnnotationDao(
        database: ReadSphereDatabase
    ): AnnotationDao {
        return database.annotationDao()
    }
}

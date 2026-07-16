package com.readsphere.app.di

import com.readsphere.app.data.repository.AnnotationRepositoryImpl
import com.readsphere.app.data.repository.BookmarkRepositoryImpl
import com.readsphere.app.data.repository.DocumentRepositoryImpl
import com.readsphere.app.data.repository.PreferenceRepositoryImpl
import com.readsphere.app.domain.repository.AnnotationRepository
import com.readsphere.app.domain.repository.BookmarkRepository
import com.readsphere.app.domain.repository.DocumentRepository
import com.readsphere.app.domain.repository.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDocumentRepository(
        impl: DocumentRepositoryImpl
    ): DocumentRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        impl: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindAnnotationRepository(
        impl: AnnotationRepositoryImpl
    ): AnnotationRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository
}

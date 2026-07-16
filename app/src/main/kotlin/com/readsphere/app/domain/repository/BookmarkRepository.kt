package com.readsphere.app.domain.repository

import com.readsphere.app.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getBookmarksForDocument(documentId: String): Flow<List<Bookmark>>
    suspend fun getBookmarkById(id: String): Bookmark?
    suspend fun insertBookmark(bookmark: Bookmark)
    suspend fun updateBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(bookmark: Bookmark)
    suspend fun deleteBookmarkById(id: String)
    suspend fun isPageBookmarked(documentId: String, pageNumber: Int): Boolean
}

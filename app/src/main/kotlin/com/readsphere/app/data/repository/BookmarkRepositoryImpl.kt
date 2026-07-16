package com.readsphere.app.data.repository

import com.readsphere.app.data.local.db.dao.BookmarkDao
import com.readsphere.app.data.local.db.entity.BookmarkEntity
import com.readsphere.app.domain.model.Bookmark
import com.readsphere.app.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override fun getBookmarksForDocument(documentId: String): Flow<List<Bookmark>> {
        return bookmarkDao.getBookmarksForDocument(documentId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getBookmarkById(id: String): Bookmark? {
        return bookmarkDao.getBookmarkById(id)?.toDomainModel()
    }

    override suspend fun insertBookmark(bookmark: Bookmark) {
        bookmarkDao.insertBookmark(BookmarkEntity.fromDomainModel(bookmark))
    }

    override suspend fun updateBookmark(bookmark: Bookmark) {
        bookmarkDao.updateBookmark(BookmarkEntity.fromDomainModel(bookmark))
    }

    override suspend fun deleteBookmark(bookmark: Bookmark) {
        bookmarkDao.deleteBookmark(BookmarkEntity.fromDomainModel(bookmark))
    }

    override suspend fun deleteBookmarkById(id: String) {
        bookmarkDao.deleteBookmarkById(id)
    }

    override suspend fun isPageBookmarked(documentId: String, pageNumber: Int): Boolean {
        return bookmarkDao.isPageBookmarked(documentId, pageNumber) > 0
    }
}

package com.readsphere.app.domain.repository

import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.DocumentSearchResult
import com.readsphere.app.domain.model.FileFilter
import com.readsphere.app.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    fun getAllDocuments(): Flow<List<Document>>
    fun getRecentDocuments(limit: Int = 10): Flow<List<Document>>
    fun getFavoriteDocuments(): Flow<List<Document>>
    fun getDocumentsByType(filter: FileFilter): Flow<List<Document>>
    fun getDocumentById(id: String): Flow<Document?>
    fun searchDocuments(query: String): Flow<List<DocumentSearchResult>>
    suspend fun insertDocument(document: Document)
    suspend fun updateDocument(document: Document)
    suspend fun deleteDocument(document: Document)
    suspend fun toggleFavorite(documentId: String)
    suspend fun updateReadingProgress(documentId: String, page: Int, totalPages: Int)
    suspend fun incrementReadingTime(documentId: String, minutes: Long)
    suspend fun getDocumentCount(): Int
    suspend fun getTotalReadingTimeThisWeek(): Long
    suspend fun clearRecentDocuments()
}

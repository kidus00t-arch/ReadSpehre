package com.readsphere.app.data.repository

import com.readsphere.app.data.local.db.dao.DocumentDao
import com.readsphere.app.data.local.db.entity.DocumentEntity
import com.readsphere.app.data.local.file.DocumentParser
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.DocumentSearchResult
import com.readsphere.app.domain.model.FileFilter
import com.readsphere.app.domain.model.FileType
import com.readsphere.app.domain.model.SortOrder
import com.readsphere.app.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepositoryImpl @Inject constructor(
    private val documentDao: DocumentDao,
    private val documentParser: DocumentParser
) : DocumentRepository {

    override fun getAllDocuments(): Flow<List<Document>> {
        return documentDao.getAllDocuments().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getRecentDocuments(limit: Int): Flow<List<Document>> {
        return documentDao.getRecentDocuments(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getFavoriteDocuments(): Flow<List<Document>> {
        return documentDao.getFavoriteDocuments().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getDocumentsByType(filter: FileFilter): Flow<List<Document>> {
        return if (filter == FileFilter.All) {
            getAllDocuments()
        } else {
            val fileTypeName = when (filter) {
                FileFilter.PDF -> FileType.PDF.name
                FileFilter.DOCX -> FileType.DOCX.name
                FileFilter.PPTX -> FileType.PPTX.name
                else -> ""
            }
            documentDao.getDocumentsByType(fileTypeName).map { entities ->
                entities.map { it.toDomainModel() }
            }
        }
    }

    override fun getDocumentById(id: String): Flow<Document?> {
        return documentDao.getDocumentById(id).map { it?.toDomainModel() }
    }

    override fun searchDocuments(query: String): Flow<List<DocumentSearchResult>> {
        return documentDao.searchDocumentsByTitle(query).map { entities ->
            entities.map { entity ->
                DocumentSearchResult(
                    document = entity.toDomainModel(),
                    matchedText = null
                )
            }
        }
    }

    override suspend fun insertDocument(document: Document) {
        documentDao.insertDocument(DocumentEntity.fromDomainModel(document))
    }

    override suspend fun updateDocument(document: Document) {
        documentDao.updateDocument(DocumentEntity.fromDomainModel(document))
    }

    override suspend fun deleteDocument(document: Document) {
        documentDao.deleteDocument(DocumentEntity.fromDomainModel(document))
    }

    override suspend fun toggleFavorite(documentId: String) {
        documentDao.toggleFavorite(documentId)
    }

    override suspend fun updateReadingProgress(documentId: String, page: Int, totalPages: Int) {
        documentDao.updateProgress(documentId, page, totalPages, System.currentTimeMillis())
    }

    override suspend fun incrementReadingTime(documentId: String, minutes: Long) {
        documentDao.incrementReadingTime(documentId, minutes)
    }

    override suspend fun getDocumentCount(): Int {
        return documentDao.getDocumentCount()
    }

    override suspend fun getTotalReadingTimeThisWeek(): Long {
        val weekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
        return documentDao.getTotalReadingTimeSince(weekAgo) ?: 0L
    }

    override suspend fun clearRecentDocuments() {
        // Implementation: could keep documents but clear lastOpenedAt
    }
}

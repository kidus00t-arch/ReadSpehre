package com.readsphere.app.domain.usecase

import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.FileFilter
import com.readsphere.app.domain.model.SortOrder
import com.readsphere.app.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    fun getAllDocuments(sortOrder: SortOrder = SortOrder.DateModified): Flow<List<Document>> {
        return documentRepository.getAllDocuments().map { documents ->
            sortDocuments(documents, sortOrder)
        }
    }

    fun getRecentDocuments(limit: Int = 10): Flow<List<Document>> {
        return documentRepository.getRecentDocuments(limit)
    }

    fun getFavoriteDocuments(): Flow<List<Document>> {
        return documentRepository.getFavoriteDocuments()
    }

    fun getDocumentsByType(filter: FileFilter, sortOrder: SortOrder = SortOrder.DateModified): Flow<List<Document>> {
        return documentRepository.getDocumentsByType(filter).map { documents ->
            sortDocuments(documents, sortOrder)
        }
    }

    private fun sortDocuments(documents: List<Document>, sortOrder: SortOrder): List<Document> {
        return when (sortOrder) {
            SortOrder.Name -> documents.sortedBy { it.title.lowercase() }
            SortOrder.DateModified -> documents.sortedByDescending { it.modifiedAt }
            SortOrder.DateCreated -> documents.sortedByDescending { it.createdAt }
            SortOrder.Size -> documents.sortedByDescending { it.size }
            SortOrder.Type -> documents.sortedBy { it.fileType.label }
        }
    }
}

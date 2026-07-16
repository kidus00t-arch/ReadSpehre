package com.readsphere.app.domain.usecase

import com.readsphere.app.domain.model.DocumentSearchResult
import com.readsphere.app.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDocumentsUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    operator fun invoke(query: String): Flow<List<DocumentSearchResult>> {
        return documentRepository.searchDocuments(query)
    }
}

package com.readsphere.app.domain.usecase

import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.repository.DocumentRepository
import java.util.Date
import javax.inject.Inject

class OpenDocumentUseCase @Inject constructor(
    private val documentRepository: DocumentRepository
) {
    suspend operator fun invoke(document: Document) {
        val updated = document.copy(
            lastOpenedAt = Date()
        )
        documentRepository.updateDocument(updated)
    }
}

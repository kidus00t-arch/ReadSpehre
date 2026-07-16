package com.readsphere.app.domain.repository

import com.readsphere.app.domain.model.Annotation
import com.readsphere.app.domain.model.AnnotationType
import kotlinx.coroutines.flow.Flow

interface AnnotationRepository {
    fun getAnnotationsForDocument(documentId: String): Flow<List<Annotation>>
    fun getAnnotationsForPage(documentId: String, pageNumber: Int): Flow<List<Annotation>>
    suspend fun getAnnotationById(id: String): Annotation?
    suspend fun insertAnnotation(annotation: Annotation)
    suspend fun updateAnnotation(annotation: Annotation)
    suspend fun deleteAnnotation(annotation: Annotation)
    suspend fun deleteAnnotationsForDocument(documentId: String)
}

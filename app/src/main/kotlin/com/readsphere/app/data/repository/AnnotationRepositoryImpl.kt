package com.readsphere.app.data.repository

import com.readsphere.app.data.local.db.dao.AnnotationDao
import com.readsphere.app.data.local.db.entity.AnnotationEntity
import com.readsphere.app.domain.model.Annotation
import com.readsphere.app.domain.repository.AnnotationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnnotationRepositoryImpl @Inject constructor(
    private val annotationDao: AnnotationDao
) : AnnotationRepository {

    override fun getAnnotationsForDocument(documentId: String): Flow<List<Annotation>> {
        return annotationDao.getAnnotationsForDocument(documentId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAnnotationsForPage(documentId: String, pageNumber: Int): Flow<List<Annotation>> {
        return annotationDao.getAnnotationsForPage(documentId, pageNumber).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getAnnotationById(id: String): Annotation? {
        return annotationDao.getAnnotationById(id)?.toDomainModel()
    }

    override suspend fun insertAnnotation(annotation: Annotation) {
        annotationDao.insertAnnotation(AnnotationEntity.fromDomainModel(annotation))
    }

    override suspend fun updateAnnotation(annotation: Annotation) {
        annotationDao.updateAnnotation(AnnotationEntity.fromDomainModel(annotation))
    }

    override suspend fun deleteAnnotation(annotation: Annotation) {
        annotationDao.deleteAnnotation(AnnotationEntity.fromDomainModel(annotation))
    }

    override suspend fun deleteAnnotationsForDocument(documentId: String) {
        annotationDao.deleteAnnotationsForDocument(documentId)
    }
}

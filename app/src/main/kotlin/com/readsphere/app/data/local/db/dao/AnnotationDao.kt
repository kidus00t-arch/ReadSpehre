package com.readsphere.app.data.local.db.dao

import androidx.room.*
import com.readsphere.app.data.local.db.entity.AnnotationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationDao {
    @Query("SELECT * FROM annotations WHERE documentId = :documentId ORDER BY pageNumber ASC, createdAt ASC")
    fun getAnnotationsForDocument(documentId: String): Flow<List<AnnotationEntity>>

    @Query("SELECT * FROM annotations WHERE documentId = :documentId AND pageNumber = :pageNumber ORDER BY createdAt ASC")
    fun getAnnotationsForPage(documentId: String, pageNumber: Int): Flow<List<AnnotationEntity>>

    @Query("SELECT * FROM annotations WHERE id = :id")
    suspend fun getAnnotationById(id: String): AnnotationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnotation(annotation: AnnotationEntity)

    @Update
    suspend fun updateAnnotation(annotation: AnnotationEntity)

    @Delete
    suspend fun deleteAnnotation(annotation: AnnotationEntity)

    @Query("DELETE FROM annotations WHERE documentId = :documentId")
    suspend fun deleteAnnotationsForDocument(documentId: String)
}

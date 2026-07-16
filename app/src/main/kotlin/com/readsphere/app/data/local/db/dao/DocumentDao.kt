package com.readsphere.app.data.local.db.dao

import androidx.room.*
import com.readsphere.app.data.local.db.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY lastOpenedAt DESC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents ORDER BY lastOpenedAt DESC LIMIT :limit")
    fun getRecentDocuments(limit: Int): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE isFavorite = 1 ORDER BY modifiedAt DESC")
    fun getFavoriteDocuments(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents ORDER BY lastOpenedAt DESC")
    fun getAllDocumentsList(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE fileType = :fileType ORDER BY modifiedAt DESC")
    fun getDocumentsByType(fileType: String): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE id = :id")
    fun getDocumentById(id: String): Flow<DocumentEntity?>

    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocumentByIdSync(id: String): DocumentEntity?

    @Query("""
        SELECT * FROM documents 
        WHERE title LIKE '%' || :query || '%' 
        ORDER BY lastOpenedAt DESC
    """)
    fun searchDocumentsByTitle(query: String): Flow<List<DocumentEntity>>

    @Query("SELECT COUNT(*) FROM documents")
    suspend fun getDocumentCount(): Int

    @Query("SELECT SUM(totalReadingTimeMinutes) FROM documents WHERE lastOpenedAt >= :sinceTimestamp")
    suspend fun getTotalReadingTimeSince(sinceTimestamp: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity)

    @Update
    suspend fun updateDocument(document: DocumentEntity)

    @Delete
    suspend fun deleteDocument(document: DocumentEntity)

    @Query("UPDATE documents SET isFavorite = CASE WHEN isFavorite = 0 THEN 1 ELSE 0 END WHERE id = :documentId")
    suspend fun toggleFavorite(documentId: String)

    @Query("UPDATE documents SET currentPage = :page, readingProgress = CAST(:page AS REAL) / NULLIF(:totalPages, 0), lastOpenedAt = :timestamp WHERE id = :documentId")
    suspend fun updateProgress(documentId: String, page: Int, totalPages: Int, timestamp: Long)

    @Query("UPDATE documents SET totalReadingTimeMinutes = totalReadingTimeMinutes + :minutes WHERE id = :documentId")
    suspend fun incrementReadingTime(documentId: String, minutes: Long)
}

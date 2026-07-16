package com.readsphere.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.FileType

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val fileType: String,
    val filePath: String,
    val uri: String?,
    val size: Long,
    val pageCount: Int,
    val wordCount: Int,
    val author: String?,
    val isFavorite: Boolean,
    val isPasswordProtected: Boolean,
    val lastOpenedAt: Long?,
    val createdAt: Long,
    val modifiedAt: Long,
    val readingProgress: Float,
    val currentPage: Int,
    val totalReadingTimeMinutes: Long,
    val thumbnailPath: String?,
    val tags: String // JSON serialized list
) {
    fun toDomainModel(): Document = Document(
        id = id,
        title = title,
        fileType = FileType.valueOf(fileType),
        filePath = filePath,
        uri = uri,
        size = size,
        pageCount = pageCount,
        wordCount = wordCount,
        author = author,
        isFavorite = isFavorite,
        isPasswordProtected = isPasswordProtected,
        lastOpenedAt = lastOpenedAt?.let { java.util.Date(it) },
        createdAt = java.util.Date(createdAt),
        modifiedAt = java.util.Date(modifiedAt),
        readingProgress = readingProgress,
        currentPage = currentPage,
        totalReadingTimeMinutes = totalReadingTimeMinutes,
        thumbnailPath = thumbnailPath,
        tags = if (tags.isBlank()) emptyList() else tags.split(",").map { it.trim() }
    )

    companion object {
        fun fromDomainModel(document: Document): DocumentEntity = DocumentEntity(
            id = document.id,
            title = document.title,
            fileType = document.fileType.name,
            filePath = document.filePath,
            uri = document.uri,
            size = document.size,
            pageCount = document.pageCount,
            wordCount = document.wordCount,
            author = document.author,
            isFavorite = document.isFavorite,
            isPasswordProtected = document.isPasswordProtected,
            lastOpenedAt = document.lastOpenedAt?.time,
            createdAt = document.createdAt.time,
            modifiedAt = document.modifiedAt.time,
            readingProgress = document.readingProgress,
            currentPage = document.currentPage,
            totalReadingTimeMinutes = document.totalReadingTimeMinutes,
            thumbnailPath = document.thumbnailPath,
            tags = document.tags.joinToString(",")
        )
    }
}

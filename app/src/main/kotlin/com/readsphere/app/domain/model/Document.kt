package com.readsphere.app.domain.model

import java.util.Date
import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val fileType: FileType,
    val filePath: String,
    val uri: String? = null,
    val size: Long = 0,
    val pageCount: Int = 0,
    val wordCount: Int = 0,
    val author: String? = null,
    val isFavorite: Boolean = false,
    val isPasswordProtected: Boolean = false,
    val lastOpenedAt: Date? = null,
    val createdAt: Date = Date(),
    val modifiedAt: Date = Date(),
    val readingProgress: Float = 0f,
    val currentPage: Int = 0,
    val totalReadingTimeMinutes: Long = 0,
    val thumbnailPath: String? = null,
    val tags: List<String> = emptyList()
)

enum class FileType(val label: String, val extensions: List<String>, val mimeType: String) {
    PDF("PDF", listOf("pdf"), "application/pdf"),
    DOCX("Word", listOf("docx", "doc"), "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PPTX("PowerPoint", listOf("pptx", "ppt"), "application/vnd.openxmlformats-officedocument.presentationml.presentation");

    companion object {
        fun fromExtension(extension: String): FileType? {
            return entries.firstOrNull { extension.lowercase() in it.extensions }
        }

        fun fromMimeType(mimeType: String): FileType? {
            return entries.firstOrNull { it.mimeType == mimeType }
        }

        fun fromPath(path: String): FileType? {
            val ext = path.substringAfterLast('.', "").lowercase()
            return fromExtension(ext)
        }
    }
}

data class DocumentSearchResult(
    val document: Document,
    val matchedText: String? = null,
    val pageNumber: Int? = null
)

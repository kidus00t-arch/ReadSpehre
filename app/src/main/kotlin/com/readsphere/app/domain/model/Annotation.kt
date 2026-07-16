package com.readsphere.app.domain.model

import java.util.Date
import java.util.UUID

data class Annotation(
    val id: String = UUID.randomUUID().toString(),
    val documentId: String,
    val pageNumber: Int,
    val type: AnnotationType,
    val color: String = "#FFEB3B",
    val content: String? = null,
    val rectLeft: Float? = null,
    val rectTop: Float? = null,
    val rectRight: Float? = null,
    val rectBottom: Float? = null,
    val pathData: String? = null, // For freehand drawings
    val createdAt: Date = Date(),
    val modifiedAt: Date = Date()
)

enum class AnnotationType(val label: String) {
    Highlight("Highlight"),
    Underline("Underline"),
    Strikethrough("Strikethrough"),
    Note("Note"),
    Drawing("Drawing"),
    VoiceNote("Voice Note")
}

data class VoiceNote(
    val id: String = UUID.randomUUID().toString(),
    val documentId: String,
    val pageNumber: Int,
    val filePath: String,
    val durationMs: Long,
    val createdAt: Date = Date()
)

enum class ExportFormat(val label: String, val extension: String) {
    PDF("PDF", "pdf"),
    TXT("Text", "txt"),
    Markdown("Markdown", "md")
}

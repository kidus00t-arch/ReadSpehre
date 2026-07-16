package com.readsphere.app.data.local.file

import android.content.Context
import android.net.Uri
import com.readsphere.app.domain.model.Document
import com.readsphere.app.domain.model.FileType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for parsing documents and extracting metadata.
 * This provides a unified interface for different document formats.
 */
@Singleton
class DocumentParser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    data class ParsedDocument(
        val title: String,
        val fileType: FileType,
        val pageCount: Int,
        val wordCount: Int = 0,
        val author: String? = null,
        val isPasswordProtected: Boolean = false
    )

    /**
     * Parse a document from a file path and return metadata
     */
    fun parseDocument(filePath: String): ParsedDocument {
        val fileType = FileType.fromPath(filePath) ?: FileType.PDF
        val fileName = filePath.substringAfterLast('/')
            .substringAfterLast('\\')
            .substringBeforeLast('.')
            .ifBlank { "Untitled" }

        return when (fileType) {
            FileType.PDF -> parsePdf(filePath, fileName)
            FileType.DOCX -> parseDocx(filePath, fileName)
            FileType.PPTX -> parsePptx(filePath, fileName)
        }
    }

    /**
     * Parse a document from a URI and return metadata
     */
    fun parseDocumentFromUri(uri: Uri, fileName: String): ParsedDocument {
        val fileType = FileType.fromPath(fileName) ?: FileType.PDF
        val name = fileName.substringBeforeLast('.').ifBlank { "Untitled" }

        return when (fileType) {
            FileType.PDF -> ParsedDocument(name, FileType.PDF, pageCount = 1)
            FileType.DOCX -> ParsedDocument(name, FileType.DOCX, pageCount = 1)
            FileType.PPTX -> ParsedDocument(name, FileType.PPTX, pageCount = 1)
        }
    }

    /**
     * Extract text content for search indexing
     */
    fun extractTextContent(filePath: String): String {
        return try {
            val fileType = FileType.fromPath(filePath)
            when (fileType) {
                FileType.PDF -> extractPdfText(filePath)
                FileType.DOCX -> extractDocxText(filePath)
                FileType.PPTX -> extractPptxText(filePath)
                else -> ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun parsePdf(filePath: String, fileName: String): ParsedDocument {
        return try {
            // Attempt to get page count using Android PdfRenderer
            val fd = android.os.ParcelFileDescriptor.open(
                java.io.File(filePath),
                android.os.ParcelFileDescriptor.MODE_READ_ONLY
            )
            val renderer = android.graphics.pdf.PdfRenderer(fd)
            val pageCount = renderer.pageCount
            renderer.close()
            fd.close()

            ParsedDocument(
                title = fileName,
                fileType = FileType.PDF,
                pageCount = pageCount,
                wordCount = 0,
                isPasswordProtected = false
            )
        } catch (e: Exception) {
            // Fallback with basic info
            ParsedDocument(
                title = fileName,
                fileType = FileType.PDF,
                pageCount = 1,
                isPasswordProtected = e.message?.contains("password", ignoreCase = true) == true
            )
        }
    }

    private fun parseDocx(filePath: String, fileName: String): ParsedDocument {
        return try {
            // Basic parsing without Apache POI (heavy) - get file-based info
            val file = java.io.File(filePath)
            val size = file.length()

            ParsedDocument(
                title = fileName,
                fileType = FileType.DOCX,
                pageCount = (size / 2048).toInt().coerceAtLeast(1),
                wordCount = (size / 32).toInt(),
                isPasswordProtected = false
            )
        } catch (e: Exception) {
            ParsedDocument(
                title = fileName,
                fileType = FileType.DOCX,
                pageCount = 1,
                isPasswordProtected = false
            )
        }
    }

    private fun parsePptx(filePath: String, fileName: String): ParsedDocument {
        return try {
            val file = java.io.File(filePath)
            val size = file.length()

            ParsedDocument(
                title = fileName,
                fileType = FileType.PPTX,
                pageCount = (size / 3072).toInt().coerceAtLeast(1),
                wordCount = (size / 64).toInt(),
                isPasswordProtected = false
            )
        } catch (e: Exception) {
            ParsedDocument(
                title = fileName,
                fileType = FileType.PPTX,
                pageCount = 1,
                isPasswordProtected = false
            )
        }
    }

    private fun extractPdfText(filePath: String): String {
        // Basic text extraction placeholder
        // In production, use iText or similar library
        return ""
    }

    private fun extractDocxText(filePath: String): String {
        // Would use Apache POI to extract text content
        return try {
            val file = java.io.File(filePath)
            if (!file.exists()) return ""

            // Read raw file and attempt to extract text from XML
            val inputStream = file.inputStream()
            val bytes = inputStream.readBytes()
            inputStream.close()

            // Basic DOCX text extraction (stored in word/document.xml)
            val content = String(bytes)
            val documentStart = content.indexOf("<w:document")
            if (documentStart >= 0) {
                // Simple XML text extraction
                val text = StringBuilder()
                val regex = Regex("<w:t[^>]*>([^<]+)</w:t>")
                regex.findAll(content).forEach { match ->
                    text.append(match.groupValues[1]).append(" ")
                }
                text.toString()
            } else ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun extractPptxText(filePath: String): String {
        // Would use Apache POI to extract text from slides
        return try {
            val file = java.io.File(filePath)
            if (!file.exists()) return ""

            val inputStream = file.inputStream()
            val bytes = inputStream.readBytes()
            inputStream.close()

            val content = String(bytes)
            val text = StringBuilder()
            val regex = Regex("<a:t[^>]*>([^<]+)</a:t>")
            regex.findAll(content).forEach { match ->
                text.append(match.groupValues[1]).append(" ")
            }
            text.toString()
        } catch (e: Exception) {
            ""
        }
    }
}

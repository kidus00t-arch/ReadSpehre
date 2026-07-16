package com.readsphere.app.data.local.file

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileCacheManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val cacheDir = File(context.cacheDir, "documents").apply { mkdirs() }
    private val thumbnailsDir = File(context.cacheDir, "thumbnails").apply { mkdirs() }
    private val sharedDir = File(context.cacheDir, "shared").apply { mkdirs() }

    /**
     * Cache a file from a URI and return the cached file path
     */
    fun cacheFile(uri: Uri, fileName: String): String? {
        return try {
            val cachedFile = File(cacheDir, sanitizeFileName(fileName))
            if (cachedFile.exists()) {
                return cachedFile.absolutePath
            }

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(cachedFile).use { output ->
                    input.copyTo(output)
                }
            }
            cachedFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get cached file if it exists, otherwise return null
     */
    fun getCachedFile(fileName: String): File? {
        val file = File(cacheDir, sanitizeFileName(fileName))
        return if (file.exists()) file else null
    }

    /**
     * Save a thumbnail for a document
     */
    fun saveThumbnail(documentId: String, bitmapBytes: ByteArray): String? {
        return try {
            val thumbFile = File(thumbnailsDir, "${documentId}.thumb")
            FileOutputStream(thumbFile).use { it.write(bitmapBytes) }
            thumbFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get thumbnail file for a document
     */
    fun getThumbnailFile(documentId: String): File? {
        val file = File(thumbnailsDir, "${documentId}.thumb")
        return if (file.exists()) file else null
    }

    /**
     * Create a shared copy of a file for sharing intents
     */
    fun createSharedFile(filePath: String, fileName: String): File? {
        return try {
            val source = File(filePath)
            val shared = File(sharedDir, sanitizeFileName(fileName))
            source.copyTo(shared, overwrite = true)
            shared
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get total cache size
     */
    fun getCacheSize(): Long {
        return cacheDir.totalSpace - cacheDir.freeSpace
    }

    /**
     * Clear all cached documents
     */
    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
        thumbnailsDir.listFiles()?.forEach { it.delete() }
        sharedDir.listFiles()?.forEach { it.delete() }
    }

    /**
     * Clear expired cache (files not accessed in 7 days)
     */
    fun clearExpiredCache() {
        val sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
        cacheDir.listFiles()?.forEach { file ->
            if (file.lastModified() < sevenDaysAgo) {
                file.delete()
            }
        }
    }

    private fun sanitizeFileName(fileName: String): String {
        return fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_")
    }
}

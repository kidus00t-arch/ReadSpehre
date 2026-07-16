package com.readsphere.app.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Format file size to human-readable string
 */
fun Long.formatFileSize(): String {
    val units = arrayOf("B", "KB", "MB", "GB")
    var size = this.toFloat()
    var unitIndex = 0
    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }
    return "%.1f %s".format(size, units[unitIndex])
}

/**
 * Format a duration in minutes to human-readable string
 */
fun Long.formatDuration(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m"
        else -> "<1m"
    }
}

/**
 * Format a date to a relative time string
 */
fun Date.formatRelativeTime(): String {
    val now = System.currentTimeMillis()
    val diff = now - this.time

    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        diff < 604_800_000 -> "${diff / 86_400_000}d ago"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(this)
    }
}

/**
 * Format date to a standard display format
 */
fun Date.formatDisplayDate(): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(this)
}

/**
 * Format date with time
 */
fun Date.formatDateTime(): String {
    return SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(this)
}

/**
 * Get file extension from a path
 */
fun String.getFileExtension(): String {
    return substringAfterLast('.', "").lowercase()
}

/**
 * Get file name from a path
 */
fun String.getFileName(): String {
    return substringAfterLast('/').substringAfterLast('\\')
}

/**
 * Get file name without extension
 */
fun String.getFileNameWithoutExtension(): String {
    return getFileName().substringBeforeLast('.')
}

/**
 * Share a file using Android share intent
 */
fun Context.shareFile(filePath: String, mimeType: String) {
    try {
        val file = File(filePath)
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(Intent.createChooser(intent, "Share"))
    } catch (e: Exception) {
        Toast.makeText(this, "Failed to share file", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Show a toast message
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Boolean to int conversion
 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * Safe division
 */
fun Float.safeDivide(other: Float): Float {
    return if (other == 0f) 0f else this / other
}

/**
 * Clamp int between min and max
 */
fun Int.clamp(min: Int, max: Int): Int = when {
    this < min -> min
    this > max -> max
    else -> this
}

/**
 * Clamp float between min and max
 */
fun Float.clamp(min: Float, max: Float): Float = when {
    this < min -> min
    this > max -> max
    else -> this
}

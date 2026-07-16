package com.readsphere.app.core.util

object Constants {
    const val APP_NAME = "ReadSphere"
    const val APP_VERSION = "1.0.0"

    // Database
    const val DATABASE_NAME = "readsphere.db"

    // Preferences
    const val PREFERENCES_NAME = "readsphere_preferences"

    // File types
    const val PDF_MIME_TYPE = "application/pdf"
    const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    const val PPTX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    const val DOC_MIME_TYPE = "application/msword"
    const val PPT_MIME_TYPE = "application/vnd.ms-powerpoint"

    const val PDF_EXTENSION = "pdf"
    const val DOCX_EXTENSION = "docx"
    const val PPTX_EXTENSION = "pptx"

    // Time constants
    const val MILLIS_PER_MINUTE = 60_000L
    const val MILLIS_PER_HOUR = 3_600_000L
    const val MILLIS_PER_DAY = 86_400_000L
    const val MILLIS_PER_WEEK = 604_800_000L

    // Limits
    const val MAX_RECENT_DOCUMENTS = 50
    const val MAX_CACHE_SIZE_BYTES = 100L * 1024 * 1024 // 100 MB
    const val MAX_FILE_SIZE_BYTES = 50L * 1024 * 1024 // 50 MB
    const val THUMBNAIL_WIDTH = 256
    const val THUMBNAIL_HEIGHT = 320

    // Cache
    const val CACHE_EXPIRY_DAYS = 7

    // Reading
    const val MIN_FONT_SIZE = 12
    const val MAX_FONT_SIZE = 28
    const val DEFAULT_FONT_SIZE = 16

    // Search
    const val SEARCH_DEBOUNCE_MS = 300L

    // Animation
    const val DEFAULT_ANIMATION_DURATION = 300

    // Navigation
    const val HOME_ROUTE = "home"
    const val LIBRARY_ROUTE = "library"
    const val SEARCH_ROUTE = "search"
    const val SETTINGS_ROUTE = "settings"
    const val READER_ROUTE = "reader/{documentId}"
    const val ONBOARDING_ROUTE = "onboarding"

    // Argument keys
    const val ARG_DOCUMENT_ID = "documentId"
    const val ARG_DOCUMENT_PATH = "documentPath"
    const val ARG_FILE_URI = "fileUri"
    const val ARG_FILE_TYPE = "fileType"
    const val ARG_PAGE_NUMBER = "pageNumber"
}

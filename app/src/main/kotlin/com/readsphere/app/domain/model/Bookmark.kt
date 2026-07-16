package com.readsphere.app.domain.model

import java.util.Date
import java.util.UUID

data class Bookmark(
    val id: String = UUID.randomUUID().toString(),
    val documentId: String,
    val pageNumber: Int,
    val title: String = "",
    val color: String = "#FFEB3B", // Default yellow
    val note: String? = null,
    val createdAt: Date = Date()
)

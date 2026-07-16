package com.readsphere.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.readsphere.app.domain.model.Bookmark

@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("documentId"), Index("documentId", "pageNumber", unique = true)]
)
data class BookmarkEntity(
    @PrimaryKey
    val id: String,
    val documentId: String,
    val pageNumber: Int,
    val title: String,
    val color: String,
    val note: String?,
    val createdAt: Long
) {
    fun toDomainModel(): Bookmark = Bookmark(
        id = id,
        documentId = documentId,
        pageNumber = pageNumber,
        title = title,
        color = color,
        note = note,
        createdAt = java.util.Date(createdAt)
    )

    companion object {
        fun fromDomainModel(bookmark: Bookmark): BookmarkEntity = BookmarkEntity(
            id = bookmark.id,
            documentId = bookmark.documentId,
            pageNumber = bookmark.pageNumber,
            title = bookmark.title,
            color = bookmark.color,
            note = bookmark.note,
            createdAt = bookmark.createdAt.time
        )
    }
}

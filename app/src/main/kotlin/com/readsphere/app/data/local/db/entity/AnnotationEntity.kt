package com.readsphere.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.readsphere.app.domain.model.Annotation
import com.readsphere.app.domain.model.AnnotationType

@Entity(
    tableName = "annotations",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("documentId"), Index("documentId", "pageNumber")]
)
data class AnnotationEntity(
    @PrimaryKey
    val id: String,
    val documentId: String,
    val pageNumber: Int,
    val type: String,
    val color: String,
    val content: String?,
    val rectLeft: Float?,
    val rectTop: Float?,
    val rectRight: Float?,
    val rectBottom: Float?,
    val pathData: String?,
    val createdAt: Long,
    val modifiedAt: Long
) {
    fun toDomainModel(): Annotation = Annotation(
        id = id,
        documentId = documentId,
        pageNumber = pageNumber,
        type = AnnotationType.valueOf(type),
        color = color,
        content = content,
        rectLeft = rectLeft,
        rectTop = rectTop,
        rectRight = rectRight,
        rectBottom = rectBottom,
        pathData = pathData,
        createdAt = java.util.Date(createdAt),
        modifiedAt = java.util.Date(modifiedAt)
    )

    companion object {
        fun fromDomainModel(annotation: Annotation): AnnotationEntity = AnnotationEntity(
            id = annotation.id,
            documentId = annotation.documentId,
            pageNumber = annotation.pageNumber,
            type = annotation.type.name,
            color = annotation.color,
            content = annotation.content,
            rectLeft = annotation.rectLeft,
            rectTop = annotation.rectTop,
            rectRight = annotation.rectRight,
            rectBottom = annotation.rectBottom,
            pathData = annotation.pathData,
            createdAt = annotation.createdAt.time,
            modifiedAt = annotation.modifiedAt.time
        )
    }
}

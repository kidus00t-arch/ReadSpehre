package com.readsphere.app.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.readsphere.app.data.local.db.dao.AnnotationDao
import com.readsphere.app.data.local.db.dao.BookmarkDao
import com.readsphere.app.data.local.db.dao.DocumentDao
import com.readsphere.app.data.local.db.entity.AnnotationEntity
import com.readsphere.app.data.local.db.entity.BookmarkEntity
import com.readsphere.app.data.local.db.entity.DocumentEntity

@Database(
    entities = [
        DocumentEntity::class,
        BookmarkEntity::class,
        AnnotationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ReadSphereDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun annotationDao(): AnnotationDao

    companion object {
        private const val DATABASE_NAME = "readsphere.db"

        @Volatile
        private var INSTANCE: ReadSphereDatabase? = null

        fun getInstance(context: Context): ReadSphereDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ReadSphereDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ReadSphereDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

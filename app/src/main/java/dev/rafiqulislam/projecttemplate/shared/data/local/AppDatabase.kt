package dev.rafiqulislam.projecttemplate.shared.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [
        PostEntity::class,
    ],
    version = 1,
    exportSchema = false
)

/*@TypeConverters(
    ProductWrapperConverters::class,
                PrescriptionSurveyWrapper::class
    )*/
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
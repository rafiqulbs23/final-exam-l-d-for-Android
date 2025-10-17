package dev.rafiqulislam.projecttemplate.shared.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String
)

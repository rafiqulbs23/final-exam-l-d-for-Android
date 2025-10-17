package dev.rafiqulislam.core.data.model

import dev.rafiqulislam.core.domain.entity.Task
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class TaskDto(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val dueDate: String, // Format: yyyy-MM-dd
    val completed: Boolean = false
) {
    fun toDomain(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            dueDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE),
            completed = completed
        )
    }
}

@Serializable
data class TaskRequestDto(
    val title: String,
    val description: String? = null,
    val dueDate: String // Format: yyyy-MM-dd
) {
    companion object {
        fun fromDomain(title: String, description: String?, dueDate: LocalDate): TaskRequestDto {
            return TaskRequestDto(
                title = title,
                description = description,
                dueDate = dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            )
        }
    }
}

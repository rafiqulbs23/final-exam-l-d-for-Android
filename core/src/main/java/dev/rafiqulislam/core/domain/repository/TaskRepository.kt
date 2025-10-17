package dev.rafiqulislam.core.domain.repository

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.domain.entity.Task
import java.time.LocalDate

interface TaskRepository {
    suspend fun getAllTasks(): Result<List<Task>>
    suspend fun createTask(title: String, description: String?, dueDate: LocalDate): Result<Task>
    suspend fun updateTask(id: Long, title: String, description: String?, dueDate: LocalDate): Result<Task>
    suspend fun deleteTask(id: Long): Result<Unit>
    suspend fun deleteAllTasks(): Result<Unit>
    suspend fun searchTasksByTitle(title: String): Result<List<Task>>
    suspend fun searchTasksByDueDate(dueDate: LocalDate): Result<List<Task>>
}

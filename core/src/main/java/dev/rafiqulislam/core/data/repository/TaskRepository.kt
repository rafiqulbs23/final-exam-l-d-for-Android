package dev.rafiqulislam.core.data.repository

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.data.model.Task
import dev.rafiqulislam.core.data.model.TaskRequest

interface TaskRepository {
    suspend fun getAllTasks(): Result<List<Task>>
    suspend fun createTask(taskRequest: TaskRequest): Result<Task>
    suspend fun updateTask(id: Long, taskRequest: TaskRequest): Result<Task>
    suspend fun deleteTask(id: Long): Result<Unit>
    suspend fun deleteAllTasks(): Result<Unit>
    suspend fun searchTasksByTitle(title: String): Result<List<Task>>
    suspend fun searchTasksByDueDate(dueDate: String): Result<List<Task>>
}

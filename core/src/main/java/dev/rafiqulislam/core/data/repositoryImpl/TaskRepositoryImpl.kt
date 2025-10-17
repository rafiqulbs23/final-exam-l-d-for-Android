package dev.rafiqulislam.core.data.repositoryImpl

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.data.model.TaskRequestDto
import dev.rafiqulislam.core.domain.entity.Task
import dev.rafiqulislam.core.domain.repository.TaskRepository
import dev.rafiqulislam.core.network.TaskApiService
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val apiService: TaskApiService
) : TaskRepository {

    override suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            val tasks = apiService.getAllTasks().map { it.toDomain() }
            Result.Success(tasks)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createTask(title: String, description: String?, dueDate: LocalDate): Result<Task> {
        return try {
            val request = TaskRequestDto.fromDomain(title, description, dueDate)
            val task = apiService.createTask(request).toDomain()
            Result.Success(task)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateTask(id: Long, title: String, description: String?, dueDate: LocalDate): Result<Task> {
        return try {
            val request = TaskRequestDto.fromDomain(title, description, dueDate)
            val task = apiService.updateTask(id, request).toDomain()
            Result.Success(task)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTask(id: Long): Result<Unit> {
        return try {
            apiService.deleteTask(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteAllTasks(): Result<Unit> {
        return try {
            apiService.deleteAllTasks()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchTasksByTitle(title: String): Result<List<Task>> {
        return try {
            val tasks = apiService.searchTasksByTitle(title).map { it.toDomain() }
            Result.Success(tasks)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchTasksByDueDate(dueDate: LocalDate): Result<List<Task>> {
        return try {
            val dateString = dueDate.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
            val tasks = apiService.searchTasksByDueDate(dateString).map { it.toDomain() }
            Result.Success(tasks)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
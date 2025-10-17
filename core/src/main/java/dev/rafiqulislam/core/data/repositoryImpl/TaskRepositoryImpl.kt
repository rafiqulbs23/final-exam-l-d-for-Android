package dev.rafiqulislam.core.data.repositoryImpl

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.data.model.Task
import dev.rafiqulislam.core.data.model.TaskRequest
import dev.rafiqulislam.core.data.repository.TaskRepository
import dev.rafiqulislam.core.network.TaskApiService
import dev.rafiqulislam.core.network.exception.NetworkException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskApiService: TaskApiService
) : TaskRepository {

    override suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            val response = taskApiService.getAllTasks()
            if (response.isSuccessful) {
                val taskList = response.body()?.data ?: emptyList()
                Result.Success(taskList)
            } else {
                Result.Error(NetworkException("Failed to fetch tasks: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }

    override suspend fun createTask(taskRequest: TaskRequest): Result<Task> {
        return try {
            val response = taskApiService.createTask(taskRequest)
            if (response.isSuccessful) {
                val task = response.body()?.data
                if (task != null) {
                    Result.Success(task)
                } else {
                    Result.Error(NetworkException("Task creation failed: No data returned"))
                }
            } else {
                Result.Error(NetworkException("Failed to create task: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }

    override suspend fun updateTask(id: Long, taskRequest: TaskRequest): Result<Task> {
        return try {
            val response = taskApiService.updateTask(id, taskRequest)
            if (response.isSuccessful) {
                val task = response.body()?.data
                if (task != null) {
                    Result.Success(task)
                } else {
                    Result.Error(NetworkException("Task update failed: No data returned"))
                }
            } else {
                Result.Error(NetworkException("Failed to update task: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }

    override suspend fun deleteTask(id: Long): Result<Unit> {
        return try {
            val response = taskApiService.deleteTask(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkException("Failed to delete task: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }

    override suspend fun deleteAllTasks(): Result<Unit> {
        return try {
            val response = taskApiService.deleteAllTasks()
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkException("Failed to delete all tasks: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }

    override suspend fun searchTasksByTitle(title: String): Result<List<Task>> {
        return try {
            val response = taskApiService.searchTasksByTitle(title)
            if (response.isSuccessful) {
                val taskList = response.body()?.data ?: emptyList()
                Result.Success(taskList)
            } else {
                Result.Error(NetworkException("Failed to search tasks by title: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }

    override suspend fun searchTasksByDueDate(dueDate: String): Result<List<Task>> {
        return try {
            val response = taskApiService.searchTasksByDueDate(dueDate)
            if (response.isSuccessful) {
                val taskList = response.body()?.data ?: emptyList()
                Result.Success(taskList)
            } else {
                Result.Error(NetworkException("Failed to search tasks by due date: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(NetworkException("Network error: ${e.message}"))
        }
    }
}

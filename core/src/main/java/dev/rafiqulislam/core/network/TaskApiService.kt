package dev.rafiqulislam.core.network

import dev.rafiqulislam.core.data.model.Task
import dev.rafiqulislam.core.data.model.TaskListResponse
import dev.rafiqulislam.core.data.model.TaskRequest
import dev.rafiqulislam.core.data.model.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApiService {
    
    @GET("tasks")
    suspend fun getAllTasks(): Response<TaskListResponse>
    
    @POST("tasks")
    suspend fun createTask(@Body taskRequest: TaskRequest): Response<TaskResponse>
    
    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body taskRequest: TaskRequest
    ): Response<TaskResponse>
    
    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<TaskResponse>
    
    @DELETE("tasks")
    suspend fun deleteAllTasks(): Response<TaskResponse>
    
    @GET("tasks/search")
    suspend fun searchTasksByTitle(@Query("title") title: String): Response<TaskListResponse>
    
    @GET("tasks/search")
    suspend fun searchTasksByDueDate(@Query("due_date") dueDate: String): Response<TaskListResponse>
}

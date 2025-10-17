package dev.rafiqulislam.core.network

import dev.rafiqulislam.core.data.model.TaskDto
import dev.rafiqulislam.core.data.model.TaskRequestDto
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
    suspend fun getAllTasks(): List<TaskDto>
    
    @POST("tasks")
    suspend fun createTask(@Body taskRequest: TaskRequestDto): TaskDto
    
    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body taskRequest: TaskRequestDto
    ): TaskDto
    
    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long)
    
    @DELETE("tasks")
    suspend fun deleteAllTasks()
    
    @GET("tasks/search")
    suspend fun searchTasksByTitle(@Query("title") title: String): List<TaskDto>
    
    @GET("tasks/search")
    suspend fun searchTasksByDueDate(@Query("due_date") dueDate: String): List<TaskDto>
}

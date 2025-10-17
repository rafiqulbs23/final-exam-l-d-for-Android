package dev.rafiqulislam.projecttemplate.features.tasks.data.network

import dev.rafiqulislam.projecttemplate.features.tasks.data.model.TaskDto
import dev.rafiqulislam.projecttemplate.features.tasks.data.model.TaskRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApiService {
    
    @GET("api/tasks")
    suspend fun getAllTasks(): List<TaskDto>
    
    @POST("api/tasks")
    suspend fun createTask(@Body taskRequest: TaskRequestDto): TaskDto
    
    @PUT("api/tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body taskRequest: TaskRequestDto
    ): TaskDto
    
    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long)
    
    @DELETE("api/tasks")
    suspend fun deleteAllTasks()
    
    @GET("api/tasks/search")
    suspend fun searchTasksByTitle(@Query("title") title: String): List<TaskDto>
    
    @GET("api/tasks/due")
    suspend fun searchTasksByDueDate(@Query("dueDate") dueDate: String): List<TaskDto>
}

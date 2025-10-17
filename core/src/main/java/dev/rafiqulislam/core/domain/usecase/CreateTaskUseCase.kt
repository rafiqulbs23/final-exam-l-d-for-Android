package dev.rafiqulislam.core.domain.usecase

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.domain.entity.Task
import dev.rafiqulislam.core.domain.repository.TaskRepository
import java.time.LocalDate
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        dueDate: LocalDate
    ): Result<Task> {
        return taskRepository.createTask(title, description, dueDate)
    }
}

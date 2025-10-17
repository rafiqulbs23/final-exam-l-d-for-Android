package dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.projecttemplate.features.tasks.domain.entity.Task
import dev.rafiqulislam.projecttemplate.features.tasks.domain.repository.TaskRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        id: Long,
        title: String,
        description: String?,
        dueDate: LocalDate
    ): Result<Task> {
        return taskRepository.updateTask(id, title, description, dueDate)
    }
}

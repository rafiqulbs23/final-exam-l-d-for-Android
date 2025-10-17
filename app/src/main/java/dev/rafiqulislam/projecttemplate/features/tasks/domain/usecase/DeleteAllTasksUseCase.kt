package dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.projecttemplate.features.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteAllTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return taskRepository.deleteAllTasks()
    }
}

package dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.projecttemplate.features.tasks.domain.entity.Task
import dev.rafiqulislam.projecttemplate.features.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class SearchTasksByTitleUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(title: String): Result<List<Task>> {
        return taskRepository.searchTasksByTitle(title)
    }
}

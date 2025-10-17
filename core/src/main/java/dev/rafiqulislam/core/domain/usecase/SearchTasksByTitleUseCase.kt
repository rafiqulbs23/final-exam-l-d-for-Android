package dev.rafiqulislam.core.domain.usecase

import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.domain.entity.Task
import dev.rafiqulislam.core.domain.repository.TaskRepository
import javax.inject.Inject

class SearchTasksByTitleUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(title: String): Result<List<Task>> {
        return taskRepository.searchTasksByTitle(title)
    }
}

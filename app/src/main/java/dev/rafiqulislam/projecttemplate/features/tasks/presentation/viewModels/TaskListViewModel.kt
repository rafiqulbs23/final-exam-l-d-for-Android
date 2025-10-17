package dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.data.model.Task
import dev.rafiqulislam.core.data.repository.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableLiveData(TaskListUiState())
    val uiState: LiveData<TaskListUiState> = _uiState

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            when (val result = taskRepository.getAllTasks()) {
                is Result.Success -> {
                    val sortedTasks = result.data.sortedBy { task ->
                        try {
                            LocalDate.parse(task.dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        } catch (e: Exception) {
                            LocalDate.MAX
                        }
                    }
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        tasks = sortedTasks,
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }

    fun searchTasksByTitle(title: String) {
        if (title.isEmpty()) {
            loadTasks()
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            when (val result = taskRepository.searchTasksByTitle(title)) {
                is Result.Success -> {
                    val sortedTasks = result.data.sortedBy { task ->
                        try {
                            LocalDate.parse(task.dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        } catch (e: Exception) {
                            LocalDate.MAX
                        }
                    }
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        tasks = sortedTasks,
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }

    fun searchTasksByDueDate(dueDate: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            when (val result = taskRepository.searchTasksByDueDate(dueDate)) {
                is Result.Success -> {
                    val sortedTasks = result.data.sortedBy { task ->
                        try {
                            LocalDate.parse(task.dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        } catch (e: Exception) {
                            LocalDate.MAX
                        }
                    }
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        tasks = sortedTasks,
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            when (val result = taskRepository.deleteTask(taskId)) {
                is Result.Success -> {
                    loadTasks() // Refresh the list
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        error = result.exception.message
                    )
                }
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            when (val result = taskRepository.deleteAllTasks()) {
                is Result.Success -> {
                    loadTasks() // Refresh the list
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        error = result.exception.message
                    )
                }
            }
        }
    }
}

data class TaskListUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)

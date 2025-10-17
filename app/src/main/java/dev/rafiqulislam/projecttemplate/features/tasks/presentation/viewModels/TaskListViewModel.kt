package dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.projecttemplate.features.tasks.domain.entity.Task
import dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase.DeleteAllTasksUseCase
import dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase.DeleteTaskUseCase
import dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase.GetAllTasksUseCase
import dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase.SearchTasksByDueDateUseCase
import dev.rafiqulislam.projecttemplate.features.tasks.domain.usecase.SearchTasksByTitleUseCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val searchTasksByTitleUseCase: SearchTasksByTitleUseCase,
    private val searchTasksByDueDateUseCase: SearchTasksByDueDateUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteAllTasksUseCase: DeleteAllTasksUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(TaskListUiState())
    val uiState: LiveData<TaskListUiState> = _uiState

    // Save search and filter state
    private var savedSearchQuery: String
        get() = savedStateHandle.get<String>("searchQuery") ?: ""
        set(value) = savedStateHandle.set("searchQuery", value)

    private var savedFilterDate: String
        get() = savedStateHandle.get<String>("filterDate") ?: ""
        set(value) = savedStateHandle.set("filterDate", value)

    init {
        // Restore search and filter state
        _uiState.value = _uiState.value?.copy(
            searchQuery = savedSearchQuery,
            filterDate = savedFilterDate
        )
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)

            when (val result = getAllTasksUseCase()) {
                is Result.Success -> {
                    val sortedTasks = result.data.sortedBy { task ->
                        task.dueDate
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
        savedSearchQuery = title
        _uiState.value = _uiState.value?.copy(searchQuery = title)

        if (title.isEmpty()) {
            loadTasks()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)

            when (val result = searchTasksByTitleUseCase(title)) {
                is Result.Success -> {
                    val sortedTasks = result.data.sortedBy { task ->
                        task.dueDate
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchTasksByDueDate(dueDate: String) {
        savedFilterDate = dueDate
        _uiState.value = _uiState.value?.copy(filterDate = dueDate)

        if (dueDate.isEmpty()) {
            loadTasks()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)

            try {
                val parsedDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                when (val result = searchTasksByDueDateUseCase(parsedDate)) {
                    is Result.Success -> {
                        val sortedTasks = result.data.sortedBy { task ->
                            task.dueDate
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
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    error = "Invalid date format"
                )
            }
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            when (val result = deleteTaskUseCase(taskId)) {
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
            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
            
            when (val result = deleteAllTasksUseCase()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        tasks = emptyList(),
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun clearFilter() {
        searchTasksByDueDate("")
    }
}

data class TaskListUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val filterDate: String = "",
    val error: String? = null
)

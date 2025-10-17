package dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rafiqulislam.core.base.Result
import dev.rafiqulislam.core.domain.entity.Task
import dev.rafiqulislam.core.domain.usecase.CreateTaskUseCase
import dev.rafiqulislam.core.domain.usecase.GetAllTasksUseCase
import dev.rafiqulislam.core.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    // Save form data to SavedStateHandle for rotation handling
    private var savedTitle: String
        get() = savedStateHandle.get<String>("title") ?: ""
        set(value) = savedStateHandle.set("title", value)

    private var savedDescription: String
        get() = savedStateHandle.get<String>("description") ?: ""
        set(value) = savedStateHandle.set("description", value)

    private var savedDueDate: String
        get() = savedStateHandle.get<String>("dueDate") ?: ""
        set(value) = savedStateHandle.set("dueDate", value)

    init {
        // Restore form data from SavedStateHandle
        _uiState.value = _uiState.value.copy(
            title = savedTitle,
            description = savedDescription,
            dueDate = savedDueDate
        )
    }

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            when (val result = getAllTasksUseCase()) {
                is Result.Success -> {
                    val task = result.data.find { it.id == taskId }
                    if (task != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            task = task,
                            title = task.title,
                            description = task.description ?: "",
                            dueDate = task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            error = null
                        )
                        // Save to SavedStateHandle
                        savedTitle = task.title
                        savedDescription = task.description ?: ""
                        savedDueDate = task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Task not found"
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
            }
        }
    }

    fun validateTitle(title: String) {
        savedTitle = title
        _uiState.value = _uiState.value.copy(title = title)
        
        val error = when {
            title.isEmpty() -> "Title is required"
            title.length > 50 -> "Title must be 50 characters or less"
            else -> null
        }
        
        _uiState.value = _uiState.value.copy(
            titleError = error,
            isFormValid = isFormValid(title, _uiState.value?.description ?: "", _uiState.value?.dueDate ?: "")
        )
    }

    fun validateDescription(description: String) {
        savedDescription = description
        _uiState.value = _uiState.value.copy(description = description)
        
        val error = if (description.length > 200) {
            "Description must be 200 characters or less"
        } else null
        
        _uiState.value = _uiState.value.copy(
            descriptionError = error,
            isFormValid = isFormValid(_uiState.value?.title ?: "", description, _uiState.value?.dueDate ?: "")
        )
    }

    fun validateDueDate(dueDate: String) {
        savedDueDate = dueDate
        _uiState.value = _uiState.value.copy(dueDate = dueDate)
        
        val error = when {
            dueDate.isEmpty() -> "Due date is required"
            else -> {
                try {
                    val date = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    if (date.isBefore(LocalDate.now())) {
                        "Due date must be today or later"
                    } else null
                } catch (e: Exception) {
                    "Invalid date format. Use yyyy-MM-dd"
                }
            }
        }
        
        _uiState.value = _uiState.value.copy(
            dueDateError = error,
            isFormValid = isFormValid(_uiState.value?.title ?: "", _uiState.value?.description ?: "", dueDate)
        )
    }

    private fun isFormValid(title: String, description: String, dueDate: String): Boolean {
        val titleValid = title.isNotEmpty() && title.length <= 50
        val descriptionValid = description.length <= 200
        val dueDateValid = dueDate.isNotEmpty() && try {
            val date = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
            !date.isBefore(LocalDate.now())
        } catch (e: Exception) {
            false
        }
        
        return titleValid && descriptionValid && dueDateValid
    }

    fun createTask(title: String, description: String, dueDate: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val parsedDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                when (val result = createTaskUseCase(
                    title = title,
                    description = if (description.isEmpty()) null else description,
                    dueDate = parsedDate
                )) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Invalid date format"
                )
            }
        }
    }

    fun updateTask(taskId: Long, title: String, description: String, dueDate: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val parsedDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                when (val result = updateTaskUseCase(
                    id = taskId,
                    title = title,
                    description = if (description.isEmpty()) null else description,
                    dueDate = parsedDate
                )) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Invalid date format"
                )
            }
        }
    }
}

data class AddEditTaskUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val task: Task? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val titleError: String? = null,
    val descriptionError: String? = null,
    val dueDateError: String? = null,
    val isFormValid: Boolean = false,
    val error: String? = null
)

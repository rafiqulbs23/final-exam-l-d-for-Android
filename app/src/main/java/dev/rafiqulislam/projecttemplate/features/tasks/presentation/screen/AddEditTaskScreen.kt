package dev.rafiqulislam.projecttemplate.features.tasks.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.rafiqulislam.core.data.model.Task
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels.AddEditTaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: AddEditTaskViewModel,
    taskId: Long? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Load task data if editing
    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    // Update fields when task is loaded
    LaunchedEffect(uiState.task) {
        uiState.task?.let { task ->
            title = task.title
            description = task.description ?: ""
            dueDate = task.dueDate
        }
    }

    // Handle navigation back on success
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId != null) "Edit Task" else "Add Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { 
                    title = it
                    viewModel.validateTitle(it)
                },
                label = { Text("Title *") },
                isError = uiState.titleError != null,
                supportingText = uiState.titleError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { 
                    description = it
                    viewModel.validateDescription(it)
                },
                label = { Text("Description") },
                isError = uiState.descriptionError != null,
                supportingText = uiState.descriptionError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Due Date Field
            OutlinedTextField(
                value = dueDate,
                onValueChange = { 
                    dueDate = it
                    viewModel.validateDueDate(it)
                },
                label = { Text("Due Date *") },
                placeholder = { Text("yyyy-MM-dd") },
                isError = uiState.dueDateError != null,
                supportingText = uiState.dueDateError?.let { { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    if (taskId != null) {
                        viewModel.updateTask(taskId, title, description, dueDate)
                    } else {
                        viewModel.createTask(title, description, dueDate)
                    }
                },
                enabled = uiState.isFormValid && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (taskId != null) "Update Task" else "Create Task")
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (dueDate.isNotEmpty()) {
                try {
                    LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                } catch (e: Exception) {
                    System.currentTimeMillis()
                }
            } else {
                System.currentTimeMillis()
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            dueDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                            viewModel.validateDueDate(dueDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        text = content
    )
}

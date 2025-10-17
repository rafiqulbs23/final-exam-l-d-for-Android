package dev.rafiqulislam.projecttemplate.features.tasks.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.rafiqulislam.projecttemplate.features.tasks.domain.entity.Task
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
    var uiState by remember { mutableStateOf(viewModel.uiState.value) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Observe LiveData changes
    LaunchedEffect(Unit) {
        viewModel.uiState.observeForever { newState ->
            uiState = newState
        }
    }

    // Load task data if editing
    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    // Update fields when task is loaded
    LaunchedEffect(uiState?.task) {
        uiState?.task?.let { task ->
            title = task.title
            description = task.description ?: ""
            dueDate = task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }

    // Handle navigation back on success
    LaunchedEffect(uiState?.isSuccess) {
        if (uiState?.isSuccess == true) {
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
                isError = uiState?.titleError != null,
                supportingText = uiState?.titleError?.let { { Text(it) } },
                trailingIcon = {
                    Text(
                        text = "${title.length}/50",
                        color = if (title.length > 50) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                },
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
                isError = uiState?.descriptionError != null,
                supportingText = uiState?.descriptionError?.let { { Text(it) } },
                trailingIcon = {
                    Text(
                        text = "${description.length}/200",
                        color = if (description.length > 200) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Due Date Field
            OutlinedTextField(
                value = dueDate,
                onValueChange = { 
                    // Don't allow manual input, only through date picker
                },
                label = { Text("Due Date *") },
                placeholder = { Text("Tap to select date (yyyy-MM-dd)") },
                isError = uiState?.dueDateError != null,
                supportingText = uiState?.dueDateError?.let { { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        showDatePicker = true 
                    },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { 
                        showDatePicker = true 
                    }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                }
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
                enabled = (uiState?.isFormValid == true) && (uiState?.isLoading != true),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState?.isLoading == true) {
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

        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Select Due Date") },
            text = {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.wrapContentSize()
                )
            },
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
        )
    }
}


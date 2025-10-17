package dev.rafiqulislam.projecttemplate.features.tasks.presentation.screen

import android.os.Build
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogProperties
import dev.rafiqulislam.projecttemplate.features.tasks.domain.entity.Task
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels.AddEditTaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: AddEditTaskViewModel,
    taskId: Long? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var uiState by remember { mutableStateOf(viewModel.uiState.value) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Observe LiveData changes
    LaunchedEffect(Unit) {
        viewModel.uiState.observeForever { newState ->
            uiState = newState
            // Update local form fields when ViewModel state changes
            title = newState?.title ?: ""
            description = newState?.description ?: ""
            dueDate = newState?.dueDate ?: ""
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
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { newValue ->
                    title = newValue
                    viewModel.validateTitle(newValue)
                },
                label = { Text("Title *") },
                isError = uiState?.titleError != null || title.length > 50,
                supportingText = {
                    Column {
                        uiState?.titleError?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (title.length > 50) {
                            Text(
                                text = "Character limit exceeded (${title.length}/50). Please remove ${title.length - 50} characters.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
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
                onValueChange = { newValue ->
                    description = newValue
                    viewModel.validateDescription(newValue)
                },
                label = { Text("Description") },
                isError = uiState?.descriptionError != null || description.length > 200,
                supportingText = {
                    Column {
                        uiState?.descriptionError?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (description.length > 200) {
                            Text(
                                text = "Character limit exceeded (${description.length}/200). Please remove ${description.length - 200} characters.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
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
                    // Check for validation errors and show toast
                    val errors = mutableListOf<String>()
                    
                    // Check title validation errors
                    if (uiState?.titleError != null) {
                        errors.add("Title: ${uiState?.titleError}")
                    } else if (title.length > 50) {
                        errors.add("Title: Character limit exceeded (${title.length}/50)")
                    }
                    
                    // Check description validation errors
                    if (uiState?.descriptionError != null) {
                        errors.add("Description: ${uiState?.descriptionError}")
                    } else if (description.length > 200) {
                        errors.add("Description: Character limit exceeded (${description.length}/200)")
                    }
                    
                    // Check due date validation errors
                    if (uiState?.dueDateError != null) {
                        errors.add("Due Date: ${uiState?.dueDateError}")
                    }
                    
                    if (errors.isNotEmpty()) {
                        val errorMessage = "Validation errors:\n" + errors.joinToString("\n")
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    
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
                        .plusDays(1)
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
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            ),
            text = {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier
                        .wrapContentSize()
                        .heightIn(max = 400.dp) // Limit height for landscape
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


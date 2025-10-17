package dev.rafiqulislam.projecttemplate.features.tasks.presentation.screen

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.DialogProperties
import dev.rafiqulislam.projecttemplate.features.tasks.domain.entity.Task
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels.TaskListViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToEditTask: (Long) -> Unit
) {
    var uiState by remember { mutableStateOf(viewModel.uiState.value) }
    var searchQuery by remember { mutableStateOf(uiState?.searchQuery ?: "") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var filterDate by remember { mutableStateOf(uiState?.filterDate ?: "") }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Observe snackbar message LiveData
    val snackbarMessage by viewModel.snackbarMessage.observeAsState()

    // When message changes, show snackbar
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearSnackbar()
            }
        }
    }

    // Observe LiveData changes
    LaunchedEffect(Unit) {
        viewModel.uiState.observeForever { newState ->
            uiState = newState
            // Update local state when ViewModel state changes
            searchQuery = newState?.searchQuery ?: ""
            filterDate = newState?.filterDate ?: ""
        }
    }
    var deletedTask by remember { mutableStateOf<Task?>(null) }
    var showUndoSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    // Debounced search effect
    LaunchedEffect(searchQuery, filterDate) {
        when {
            searchQuery.isNotEmpty() && searchQuery.length >= 3 -> {
                // Debounce search by 400ms
                delay(400)
                // Double-check the query length after delay to ensure it's still valid
                if (searchQuery.length >= 3) {
                    viewModel.searchTasksByTitle(searchQuery)
                }
            }
            searchQuery.isNotEmpty() && searchQuery.length < 3 -> {
                // If search query is less than 3 characters, load all tasks
                viewModel.loadTasks()
                viewModel.showSnackbar("Please enter at least 3 characters to search")
            }
            filterDate.isNotEmpty() -> {
                viewModel.searchTasksByDueDate(filterDate)
            }
            else -> {
                viewModel.loadTasks()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tasks List") },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            Icons.Default.FilterList, 
                            contentDescription = "Filter",
                            tint = if (uiState?.filterDate?.isNotEmpty() == true) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                    // Show Delete All button only when there are tasks
                    if (uiState?.tasks?.isNotEmpty() == true) {
                        IconButton(onClick = { showDeleteAllDialog = true }) {
                            Icon(
                                Icons.Default.DeleteSweep, 
                                contentDescription = "Delete All Tasks",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
            if (showSearchBar) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { 
                        searchQuery = it
                        // Update ViewModel state when user types
                        viewModel.searchTasksByTitle(it)
                    },
                    onSearch = { 
                        searchQuery = it
                        viewModel.searchTasksByTitle(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Filter Status Indicator
            if (uiState?.filterDate?.isNotEmpty() == true) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter Active",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Filtered by: ${formatDate(uiState?.filterDate ?: "")}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = { viewModel.clearFilter() }
                        ) {
                            Text("Clear")
                        }
                    }
                }
            }

            when {
                uiState?.isLoading == true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                uiState?.error != null -> {
                    ErrorMessage(
                        message = uiState?.error ?: "Unknown error",
                        onRetry = {
                            viewModel.clearFilter()
                            viewModel.loadTasks()
                        }
                    )
                }
                
                uiState?.tasks?.isEmpty() == true -> {
                    EmptyState()
                }
                
                else -> {
                    TaskList(
                        tasks = uiState?.tasks ?: emptyList(),
                        onTaskClick = onNavigateToEditTask,
                        onTaskLongPress = onNavigateToEditTask,
                        onTaskDelete = { task ->
                            deletedTask = task
                            showUndoSnackbar = true
                            viewModel.deleteTask(task.id ?: 0L)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilter = { date ->
                filterDate = date
                // Update ViewModel state when filter is applied
                viewModel.searchTasksByDueDate(date)
                showFilterDialog = false
            },
            onClearFilter = {
                filterDate = ""
                // Update ViewModel state when filter is cleared
                viewModel.clearFilter()
                showFilterDialog = false
            }
        )
    }

    // Delete All Confirmation Dialog
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text("Delete All Tasks") },
            text = { 
                Text("Are you sure you want to delete all tasks? This action cannot be undone.") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllTasks()
                        showDeleteAllDialog = false
                    }
                ) {
                    Text("Delete All", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search tasks") },
        placeholder = { Text("Enter at least 3 characters...") },
        supportingText = if (query.isNotEmpty() && query.length < 3) {
            { 
                Text(
                    text = "Enter at least 3 characters to search",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        modifier = modifier,
        singleLine = true
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Long) -> Unit,
    onTaskLongPress: (Long) -> Unit,
    onTaskDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                SwipeToDeleteTaskItem(
                    task = task,
                    onClick = { onTaskClick(task.id ?: 0L) },
                    onLongPress = { onTaskLongPress(task.id ?: 0L) },
                    onDelete = { onTaskDelete(task) }
                )
            }
        }
    }



@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun SwipeToDeleteTaskItem(
    task: Task,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffset = -200f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
    ) {
        // Delete background
        AnimatedVisibility(
            visible = offsetX < -50f,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }

        // Foreground card (the swipeable content)
        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) } // smoother drag
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onLongPress() })
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(      // 👈 only handle horizontal drags
                        onDragEnd = {
                            if (offsetX < maxOffset / 2) {
                                onDelete()
                            }
                            offsetX = 0f
                        }
                    ) { _, dragAmount ->
                        offsetX = (offsetX + dragAmount).coerceIn(maxOffset, 0f)
                    }
                }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (!task.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Due: ${formatDate(task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOverdue(task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (!task.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Due: ${formatDate(task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE))}",
                style = MaterialTheme.typography.bodySmall,
                color = if (isOverdue(task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No tasks yet",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap the + button to add your first task",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error: $message",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    } catch (e: DateTimeParseException) {
        dateString
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isOverdue(dateString: String): Boolean {
    return try {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        date.isBefore(LocalDate.now())
    } catch (e: DateTimeParseException) {
        false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApplyFilter: (String) -> Unit,
    onClearFilter: () -> Unit,
    initialDateMillis: Long? = System.currentTimeMillis()
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp) // Limit height for landscape
                .wrapContentHeight()
                .padding(vertical = 24.dp, horizontal = 4.dp),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Filter by Due Date",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtitle
                Text(
                    text = "Select a date to filter your tasks:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Date Picker
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false, // optional cleaner look
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp)
                        .heightIn(max = 350.dp) // Limit height for landscape
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onClearFilter,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear", color = MaterialTheme.colorScheme.primary)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .format(DateTimeFormatter.ISO_LOCAL_DATE)
                                onApplyFilter(selectedDate)
                            }
                        },
                        enabled = datePickerState.selectedDateMillis != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}



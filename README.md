# 📱 To-Do App - Android Final Assessment

A modern, feature-rich To-Do application built with **Kotlin**, **Jetpack Compose**, and **Clean Architecture** principles. This app demonstrates advanced Android development concepts including MVVM pattern, LiveData, ViewModel, Retrofit, Hilt dependency injection, and Material3 design.

## 🎯 Project Overview

This To-Do app is designed as a final assessment project showcasing:
- **Clean Architecture** implementation with proper separation of concerns
- **Modern Android UI** using Jetpack Compose and Material3
- **Robust State Management** with LiveData and ViewModel
- **Network Integration** using Retrofit and Coroutines
- **Advanced UX Features** including search, filtering, gestures, and validation
- **Configuration Change Handling** with proper state persistence

## 🏗️ Architecture

### Clean Architecture Layers

```
📁 app/
├── 📁 features/
│   └── 📁 tasks/
│       ├── 📁 data/           # Data Layer
│       │   ├── 📁 model/      # DTOs
│       │   ├── 📁 network/    # API Service
│       │   └── 📁 repository/ # Repository Implementation
│       ├── 📁 domain/         # Domain Layer
│       │   ├── 📁 entity/     # Domain Models
│       │   ├── 📁 repository/ # Repository Interface
│       │   └── 📁 usecase/    # Use Cases
│       └── 📁 presentation/   # Presentation Layer
│           ├── 📁 screen/     # UI Screens
│           └── 📁 viewModels/ # ViewModels
├── 📁 di/                     # Dependency Injection
├── 📁 navigation/             # Navigation
└── 📁 ui/                     # UI Theme

📁 core/                       # Core Module
├── 📁 base/                   # Base Classes
├── 📁 data/                   # Core Data
├── 📁 network/                # Network Layer
└── 📁 utils/                  # Utilities
```

### Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **State Management**: LiveData + ViewModel
- **Dependency Injection**: Hilt
- **Network**: Retrofit + OkHttp + Coroutines
- **Navigation**: Navigation Compose
- **Design System**: Material3
- **Database**: Room (for local data)
- **Serialization**: Kotlinx Serialization

## 🚀 Features

### Core Functionality
- ✅ **Create Tasks** with title, description, and due date
- ✅ **Edit Tasks** with full form validation
- ✅ **Delete Tasks** with confirmation dialog
- ✅ **Delete All Tasks** with bulk operation
- ✅ **View Task List** with sorting by due date

### Advanced Features
- 🔍 **Search Tasks** by title (minimum 3 characters with debouncing)
- 📅 **Filter Tasks** by due date with Material3 DatePicker
- 👆 **Swipe to Delete** with smooth animations
- 📱 **Long Press to Edit** for quick access
- 🔄 **Pull to Refresh** functionality
- 📱 **Rotation Handling** with state persistence
- ⚡ **Real-time Validation** with character counters
- 🎨 **Material3 Design** with modern UI components

### UX Enhancements
- 🎯 **Smart Search** with 400ms debouncing
- 📊 **Character Counters** for title (50 chars) and description (200 chars)
- ⚠️ **Input Validation** with real-time error messages
- 🍞 **Toast Messages** for user feedback
- 🔄 **Auto-refresh** after CRUD operations
- 🧹 **Auto-clear Filters** when returning from edit/create
- 📱 **Responsive Design** for different screen sizes

## 🛠️ Setup & Installation

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK API 24+ (Android 7.0)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd final_exam_lnd
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Sync Project**
   - Wait for Gradle sync to complete
   - Ensure all dependencies are downloaded

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### API Configuration

The app connects to a remote API server. The base URL is configured in `core/build.gradle.kts`:

```kotlin
buildTypes {
    debug {
        buildConfigField("String", "BASE_URL", "\"http://54.169.255.115:8080/\"")
    }
    release {
        buildConfigField("String", "BASE_URL", "\"http://54.169.255.115:8080/\"")
    }
}
```

## 📱 Screenshots & Demo

### 🎥 Video Demonstration
**Watch the app in action:** [To-Do App Demo Video](https://brainstationo365-my.sharepoint.com/:v:/g/personal/rafiqul_islam_brainstation-23_com/EfohIOJoaaBDsjWHcxQUvAYBuDfnCdxYSFfHHxAShVYsMg?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=gjZye0)

### Task List Screen
- Clean, modern task list with Material3 design
- Search functionality with real-time filtering
- Date filter with Material3 DatePicker
- Swipe-to-delete with confirmation
- Floating action button for adding tasks

### Add/Edit Task Screen
- Form validation with real-time feedback
- Character counters for input fields
- Material3 DatePicker for due date selection
- Error handling with user-friendly messages

## 🔧 Technical Implementation

### State Management
```kotlin
// ViewModel with LiveData
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    // ... other dependencies
) : ViewModel() {
    private val _uiState = MutableLiveData(TaskListUiState())
    val uiState: LiveData<TaskListUiState> = _uiState
}
```

### Network Integration
```kotlin
// Retrofit API Service
interface TaskApiService {
    @GET("api/tasks")
    suspend fun getAllTasks(): List<TaskDto>
    
    @POST("api/tasks")
    suspend fun createTask(@Body taskRequest: TaskRequestDto): TaskDto
    
    @PUT("api/tasks/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body taskRequest: TaskRequestDto): TaskDto
    
    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long)
}
```

### Dependency Injection
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideTaskApiService(@ApplicationContext context: Context): TaskApiService {
        return NetworkFactory.createService(
            context = context,
            serviceClass = TaskApiService::class.java,
        )
    }
}
```

## 🎨 UI/UX Features

### Material3 Design
- **Material3 Components**: Cards, Buttons, TextFields, DatePickers
- **Color Theming**: Dynamic color support with Material3 color system
- **Typography**: Material3 typography scale
- **Shapes**: Rounded corners and modern shape system

### Gestures & Interactions
- **Swipe to Delete**: Horizontal swipe gesture with smooth animations
- **Long Press to Edit**: Quick access to edit functionality
- **Pull to Refresh**: Refresh task list with pull gesture
- **Tap to Navigate**: Intuitive navigation between screens

### Form Validation
- **Real-time Validation**: Immediate feedback on input
- **Character Limits**: Title (50 chars), Description (200 chars)
- **Date Validation**: Due date must be today or later
- **Error Messages**: Clear, actionable error messages

## 🔄 State Persistence

### Rotation Handling
- **SavedStateHandle**: Preserves form data during configuration changes
- **ViewModel State**: Maintains UI state across rotations
- **LiveData Observation**: Automatic UI updates when state changes

### Data Flow
```
User Input → ViewModel → UseCase → Repository → API Service
                ↓
UI Updates ← LiveData ← State Management ← Data Processing
```

## 🧪 Testing

### Unit Tests
- ViewModel testing with mock dependencies
- Use case testing with mock repositories
- Repository testing with mock API services

### UI Tests
- Compose UI testing with test rules
- Navigation testing
- User interaction testing

## 📦 Dependencies

### Core Dependencies
- **Jetpack Compose**: Modern UI toolkit
- **Material3**: Design system components
- **Hilt**: Dependency injection
- **Retrofit**: Network communication
- **Room**: Local database
- **Navigation Compose**: Navigation framework

### Development Dependencies
- **Kotlin Serialization**: JSON serialization
- **OkHttp**: HTTP client
- **Coroutines**: Asynchronous programming
- **LiveData**: Reactive data streams

## 🚀 Performance Optimizations

- **Lazy Loading**: LazyColumn for efficient list rendering
- **Debounced Search**: 400ms delay to prevent excessive API calls
- **State Management**: Efficient state updates with LiveData
- **Memory Management**: Proper lifecycle handling
- **Network Optimization**: Efficient API calls with Retrofit

## 🔒 Security

- **Network Security**: Cleartext traffic configuration for development
- **Input Validation**: Client-side validation for all inputs
- **Error Handling**: Secure error messages without sensitive data

## 📋 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update existing task |
| DELETE | `/api/tasks/{id}` | Delete specific task |
| DELETE | `/api/tasks` | Delete all tasks |
| GET | `/api/tasks/search?title={title}` | Search tasks by title |
| GET | `/api/tasks/search?due_date={date}` | Search tasks by due date |

## 🎯 Future Enhancements

- [ ] **Offline Support**: Local database synchronization
- [ ] **Push Notifications**: Task reminders
- [ ] **Categories**: Task categorization
- [ ] **Priority Levels**: Task priority management
- [ ] **Dark Theme**: Theme switching
- [ ] **Widgets**: Home screen widgets
- [ ] **Export/Import**: Data backup and restore

## 📄 License

This project is created as part of a final assessment and is for educational purposes.

## 👨‍💻 Author

**Rafiqul Islam**
- Final Assessment Project
- Android Development with Kotlin & Jetpack Compose

---

## 🏆 Assessment Criteria Met

✅ **Clean Architecture**: Proper separation of data, domain, and presentation layers  
✅ **MVVM Pattern**: ViewModel with LiveData for state management  
✅ **Jetpack Compose**: Modern UI with Material3 design  
✅ **Network Integration**: Retrofit with Coroutines for API calls  
✅ **Dependency Injection**: Hilt for dependency management  
✅ **Navigation**: Navigation Compose for screen navigation  
✅ **State Persistence**: SavedStateHandle for rotation handling  
✅ **Advanced UX**: Search, filtering, gestures, and validation  
✅ **Error Handling**: Comprehensive error management  
✅ **Testing**: Unit and UI test structure  

This project demonstrates mastery of modern Android development practices and clean architecture principles.

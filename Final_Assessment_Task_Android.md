# Final Assessment Task - Android To-Do App

**Time:** 8 Hours  
**Submission:** Commit your work to Git and email the ZIP file.

---

## Candidate Instructions

Implement the To-Do app using **Kotlin**, **Jetpack Compose**, **MVVM**, **LiveData**, and **ViewModel**.

Network calls should use **Retrofit + Coroutines**.  
**Date format:** `2025-10-17`

### API Details

- **Base URL:** `http://54.169.255.115:8080`
- **API Documentation:** [Swagger UI](http://54.169.255.115:8080/swagger-ui/index.html)

#### Endpoints

| Method | Endpoint | Description |
|---------|-----------|-------------|
| GET | /tasks | List all tasks |
| POST | /tasks | Create a new task |
| PUT | /tasks/{id} | Update an existing task |
| DELETE | /tasks/{id} | Delete task by ID |
| DELETE | /tasks | Delete all tasks |
| GET | /tasks/search?title=... | Search by title |
| GET | /tasks/search?due_date=yyyy-MM-dd | Search by due date |

---

## 1. UI Implementation

### Task List Screen
- Show task details: title, description, and due date
- Sort by upcoming due date (nearest first)

### Add/Edit Task Screen
- Fields: title, description, due date
- Live validation:
  - Title: not empty, max 50 characters
  - Due Date: today or later
  - Description: optional, max 200 characters
- Show inline error messages

### UX Polish
- Save button disabled by default; enabled only when all fields are valid and data is changed
- Disable Save immediately after click; show progress indicator while saving

---

## 2. Network Calls

### Loading/Error Handling
- Show progress indicator while saving/loading
- On success: show Snackbar confirmation and navigate back
- On failure: show error Snackbar with **Retry** option
- Use **LiveData** in **ViewModel** for UI updates

---

## 3. Gestures & UX Enhancements

- Swipe-left to delete task with **Undo Snackbar**
- Long-press to edit task
- Combine **search + filter**:
  - Search by title
  - Filter by due date

---

## 4. State Management & Rotation Handling

Support both **portrait and landscape** orientations without losing:

- Scroll position  
- Unsaved task data on Add/Edit screen  
- Search query and filter state  

Use **ViewModel + SavedStateHandle** for persistence.

---

## Submission Instructions

1. **Deadline:**  
   - All submissions must be completed and submitted by **7:30 PM sharp**.  
   - **Late submissions will not be accepted.**

2. **Required Submissions:**  
   - Git Repository Link  
   - Zipped Folder containing your completed task(s)  
   - Demo Video of your project

3. **Submission Process:**  
   - Send all required materials via **email** before the deadline.

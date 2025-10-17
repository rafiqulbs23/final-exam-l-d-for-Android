package dev.rafiqulislam.projecttemplate.navigation

import kotlinx.serialization.Serializable




@Serializable
object SplashScreenNav

@Serializable
object HomeScreenNav

@Serializable
object TaskListScreenNav

@Serializable
data class AddTaskScreenNav(val taskId: Long? = null)

@Serializable
data class EditTaskScreenNav(val taskId: Long)


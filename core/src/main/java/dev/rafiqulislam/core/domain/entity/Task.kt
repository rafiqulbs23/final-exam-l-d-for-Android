package dev.rafiqulislam.core.domain.entity

import java.time.LocalDate

data class Task(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val dueDate: LocalDate,
    val completed: Boolean = false
)

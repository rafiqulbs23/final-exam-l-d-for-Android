package dev.rafiqulislam.core.utils

import kotlin.text.matches

val EMAIL_PATTERN = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
fun String.isValidEmail(): Boolean {
    return this.matches(EMAIL_PATTERN)
}
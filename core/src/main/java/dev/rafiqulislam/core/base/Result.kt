package dev.rafiqulislam.core.base


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

/**
 * Transforms the Result into a value of type [R] using [onSuccess] or [onFailure].
 */
inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Exception) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onFailure(exception)
}
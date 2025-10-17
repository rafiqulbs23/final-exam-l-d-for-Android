package dev.rafiqulislam.core.base

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.incepta.core.network.exception.UnauthorizedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {
    protected fun <T> callService(
        serviceCall: suspend () -> Result<T>,
        onStart: () -> Unit = {},
        onSuccess: (T) -> Unit,
        onError: (message: String, canRetry: Boolean) -> Unit = { _, _ -> },
        onCompleted: () -> Unit = {},
        retryCount: Int = 0
    ) {
        viewModelScope.launch {
            var attempts = 0
            while (attempts <= retryCount) {
                //onStart(attempts == 0)
                onStart()
                try {
                    serviceCall().fold(
                        onSuccess = { data ->
                            onSuccess(data)
                            onCompleted()
                            return@launch
                        },
                        onFailure = { error ->
                            if (attempts < retryCount) {
                                attempts++
                                delay(1000L * attempts) // Exponential backoff
                            } else {
//                                showErrorDialog(error.message ?: "Unknown error", error !is UnauthorizedException)
                                onError(error.message ?: "Unknown error", error !is UnauthorizedException)
                                onCompleted()
                                return@launch
                            }
                        }
                    )
                } catch (e: Exception) {
                    showErrorDialog(e.message ?: "Unknown error")
                    if (attempts < retryCount) {
                        attempts++
                        delay(1000L * attempts)
                    } else {
                        showErrorDialog(e.message ?: "Unknown error", e !is UnauthorizedException)
                        onCompleted()
                        return@launch
                    }
                }
            }
        }
    }

    protected suspend fun <T> callServiceAwait(
        serviceCall: suspend () -> Result<T>,
        onStart: () -> Unit = {},
        onError: (message: String, canRetry: Boolean) -> Unit = { _, _ -> },
        onCompleted: () -> Unit = {},
        retryCount: Int = 0
    ): T? {
        var attempts = 0
        while (attempts <= retryCount) {
            onStart()
            try {
                val result = serviceCall()
                var data: T? = null
                var failure: Throwable? = null
                result.fold(
                    onSuccess = {
                        data = it
                    },
                    onFailure = {
                        failure = it
                    }
                )
                if (data != null) {
                    onCompleted()
                    return data
                } else if (failure != null) {
                    if (attempts < retryCount) {
                        attempts++
                        kotlinx.coroutines.delay(1000L * attempts)
                    } else {
                        showErrorDialog(failure?.message ?: "Unknown error", failure !is UnauthorizedException)
                        showErrorDialog(failure?.message ?: "Unknown error", failure !is UnauthorizedException)
                        onCompleted()
                        return null
                    }
                }
            } catch (e: Exception) {
                showErrorMessage(e.message ?: "Unknown error")
                if (attempts < retryCount) {
                    attempts++
                    kotlinx.coroutines.delay(1000L * attempts)
                } else {
                    showErrorDialog(e.message ?: "Unknown error", e !is UnauthorizedException)
                    onCompleted()
                    return null
                }
            }
        }
        return null
    }

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent.asStateFlow()

    protected fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        _uiEvent.value = UiEvent.ShowSnackbar(message, actionLabel, duration)
    }

    protected fun showErrorMessage(message: String, canRetry: Boolean = false) {
        _uiEvent.value = UiEvent.ShowErrorMessage(message, canRetry)
    }

    protected fun showWarningMessage(message: String) {
        _uiEvent.value = UiEvent.ShowWarningMessage(message)
    }

    protected fun showErrorDialog(
        message: String,
        canRetry: Boolean = false,
        actionLabel: String = "Retry",
    ) {
        _uiEvent.value = UiEvent.ShowErrorDialog(message, canRetry, actionLabel)
    }

    protected fun showSuccessDialog(
        title: String = "Success",
        message: String = "Operation completed successfully.",
        isPopBackstack: Boolean = false
    ){
        _uiEvent.value = UiEvent.ShowSuccessDialog(title, message, isPopBackstack)

    }

    // Clear the event after it's consumed
    fun clearUiEvent() {
        _uiEvent.value = null
    }
}


package dev.rafiqulislam.projecttemplate.features.splash.presentation.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rafiqulislam.core.base.BaseViewModel
import dev.rafiqulislam.core.data.repository.TokenRepository
import dev.rafiqulislam.projecttemplate.navigation.HomeScreenNav
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : BaseViewModel() {

    private val _navigationEvent = MutableSharedFlow<Any>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _navigationEvent.emit(HomeScreenNav)

        }
    }
}
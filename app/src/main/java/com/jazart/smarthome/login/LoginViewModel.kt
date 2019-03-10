package com.jazart.smarthome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jazart.smarthome.usecase.FetchUserUseCase
import com.jazart.smarthome.usecase.LoginUseCase
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.Event
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val job = Job()

    private val _loginEvent = MutableLiveData<Event<String>>()
    val loginEvent: LiveData<Event<String>>
        get() = _loginEvent

    private val _loginError = MutableLiveData<Event<Error>>()
    val loginError: LiveData<Event<Error>>
        get() = _loginError

    fun login(username: String, password: String) {
        updateLoginStatus {
            val result = loginUseCase.signIn(username, password)
            when (result.status) {
                is Status.Success -> _loginEvent.postValue(Event(result.data))
                is Status.Failure -> _loginError.postValue(Event(result.error))
            }
        }
    }

    private fun updateLoginStatus(block: suspend () -> Unit): Job {
        return launch {
            try {
                block()
            } catch (e: Exception) {
                _loginError.postValue(Event(Error.INVALID_REQUEST))
            }
        }
    }

    fun logout() {

    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}
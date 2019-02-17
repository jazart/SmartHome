package com.jazart.smarthome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.jazart.smarthome.repository.FetchUserUseCase
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(private val userRepo: FetchUserUseCase) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val job = Job()

    val isLoggedIn: Boolean = false

    fun login() {
        updateLoginStatus {

        }
    }

    private fun updateLoginStatus(block: suspend() -> Unit): Job {
        return launch {
            try {
                block()
            } catch (e: Exception) {

            }
        }
    }

    fun signUp() {

    }

    fun logout() {

    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}
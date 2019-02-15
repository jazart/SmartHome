package com.jazart.smarthome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jazart.smarthome.repository.UserRepo
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val job = Job()
    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    val isLoggedIn: Boolean
        get() = user.value == null

    fun login() {
        updateLoginStatus {
            _user.value = userRepo.awaitUserCall()
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
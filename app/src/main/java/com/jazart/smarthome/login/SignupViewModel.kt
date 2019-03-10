package com.jazart.smarthome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.type.Credential
import com.graphql.type.Personal
import com.jazart.smarthome.usecase.SignupUseCase
import com.jazart.smarthome.util.Event
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SignupViewModel @Inject constructor(private val signupUseCase: SignupUseCase) : ViewModel(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _invalidLiveData = MutableLiveData<Event<String>>()

    val invalidLiveData: LiveData<Event<String>>
        get() = _invalidLiveData

    private val _signupResult = MutableLiveData<Event<String>>()
    val signupResult: LiveData<Event<String>>
        get() = _signupResult

    fun signup(info: Map<String, String>) {
        info.forEach { key, info ->
            if (info.isBlank()) {
                _invalidLiveData.value = Event("Invalid $key")
            }
        }
        sendSignup(info)
    }

    private fun sendSignup(info: Map<String, String>) {
        attemptSignup {
            withContext(Dispatchers.IO) {
                val signupNetworkResult = signupUseCase.signupUser(
                    Credential.builder().run {
                        username(info.getValue("username"))
                        password(info.getValue("password"))
                        build()
                    },
                    Personal.builder().run {
                        firstName(info.getValue("firstName"))
                        lastName(info.getValue("lastName"))
                        email(info.getValue("email"))
                        build()
                    }
                )
                when (signupNetworkResult.status) {
                    is Status.Success -> _signupResult.postValue(Event(signupNetworkResult.data))
                }
            }
        }
    }

    private fun attemptSignup(block: suspend () -> Unit): Job {
        return launch {
            try {
                block()
            } catch (e: Exception) {
                // Handle err
            }
        }
    }
}
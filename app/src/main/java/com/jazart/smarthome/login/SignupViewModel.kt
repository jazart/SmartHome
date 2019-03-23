package com.jazart.smarthome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.type.Credential
import com.graphql.type.Personal
import com.jazart.smarthome.usecase.SignupUseCase
import com.jazart.smarthome.util.ErrorType
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

    private val _signUpError = MutableLiveData<Event<String>>()
    val signUpError: LiveData<Event<String>>
        get() = _signUpError

    fun signup(info: Map<String, String>) {
        if(validate(info)) sendSignup(info)
    }

    private fun validate(info: Map<String, String>): Boolean {
        for((key, value) in info) {
            if (value.isBlank()) {
                _invalidLiveData.value = Event("Invalid $key.")
                return false
            }
        }
        return true
    }


    private fun sendSignup(info: Map<String, String>) {
        attemptSignup {
            withContext(Dispatchers.IO) {
                val signupNetworkResult = signupUseCase.signupUser(
                    Credential.builder().run {
                        username(info.getValue(USERNAME))
                        password(info.getValue(PASSWORD))
                        build()
                    },
                    Personal.builder().run {
                        firstName(info.getValue(FIRST_NAME))
                        lastName(info.getValue(LAST_NAME))
                        email(info.getValue(EMAIL))
                        build()
                    }
                )
                when (signupNetworkResult.status) {
                    is Status.Success -> _signupResult.postValue(Event(signupNetworkResult.data))
                    is Status.Failure -> {
                        val error = signupNetworkResult.error ?: return@withContext
                        _signUpError.postValue(Event(ErrorType.from(error)))
                    }
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

    companion object {
        const val USERNAME = "Username"
        const val FIRST_NAME = "First Name"
        const val LAST_NAME = "Last Name"
        const val EMAIL = "Email"
        const val PASSWORD = "Password"
        const val VERIFIED_PASSWORD =  "Verified Password"
    }
}
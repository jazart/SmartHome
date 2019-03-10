package com.jazart.smarthome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jazart.smarthome.usecase.SignupUseCase
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Event
import javax.inject.Inject

class SignupViewModel @Inject constructor(private val signupUseCase: SignupUseCase) : ViewModel() {
    private val _invalidLiveData = MutableLiveData<Event<String>>()
    val invalidLiveData: LiveData<Event<String>>
        get() = _invalidLiveData

    fun signup(info: List<String>) {
        info.forEach { userInfo ->
            if (userInfo.isBlank()) {
                _invalidLiveData.value = Event("Invalid form data")
            }
        }
        signupUseCase.signupUser(info)
    }

}
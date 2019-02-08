package com.jazart.smarthome

import androidx.lifecycle.ViewModel
import com.jazart.smarthome.models.User

class LoginViewModel(val user: User?) : ViewModel() {

    val isLoggedIn: Boolean
        get() = user != null

    fun login() {

    }

    fun signUp() {

    }

    fun logout() {

    }

}
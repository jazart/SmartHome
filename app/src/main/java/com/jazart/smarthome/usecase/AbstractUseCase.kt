package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.jazart.smarthome.network.Token

abstract class AuthenticationUseCase(private val prefs: SharedPreferences) {

    fun addToPrefs(token: Token, username: String) {
        prefs.edit().putString("jwt", token).apply()
        prefs.edit().putString("username", username).apply()
    }
}
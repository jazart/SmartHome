package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.jazart.smarthome.common.MainActivity.Companion.JWT
import com.jazart.smarthome.common.MainActivity.Companion.USERNAME
import com.jazart.smarthome.network.Token

abstract class AuthenticationUseCase(private val prefs: SharedPreferences) {

    fun addToPrefs(token: Token, username: String) {
        prefs.edit().putString(JWT, token).apply()
        prefs.edit().putString(USERNAME, username).apply()
    }
}
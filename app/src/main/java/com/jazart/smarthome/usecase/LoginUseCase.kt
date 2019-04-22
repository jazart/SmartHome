package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.graphql.LoginMutation
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val service: SmartHomeService,
    prefs: SharedPreferences
) : AuthenticationUseCase(prefs) {

    suspend fun signIn(username: String, password: String): Result<String> {
        val response = service.signin(LoginMutation.builder().run {
            name(username)
            pass(password)
            build()
        }) ?: return Result.failure(Error.NOT_FOUND)

        return when {
            response.hasErrors() -> Result.failure(ErrorType.from(response.errors()))
            else -> {
                addToPrefs(response.data()!!.login()!!, username)
                Result.success(response.data()?.login()!!)
            }
        }
    }
}
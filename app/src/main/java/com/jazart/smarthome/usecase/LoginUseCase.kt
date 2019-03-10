package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.apollographql.apollo.api.Input
import com.graphql.LoginMutation
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val service: SmartHomeService,
    prefs: SharedPreferences
) : AuthenticationUseCase(prefs) {

    suspend fun signIn(username: String, password: String): Result<String> {
        val response = service.signin(LoginMutation.builder().run {
            nameInput(Input.fromNullable(username))
            passInput(Input.fromNullable(password))
            build()
        })

        if (response.hasErrors()) {
            return Result.failure(ErrorType.from(response.errors()))
        }
        addToPrefs(response.data()!!.login()!!, username)
        return Result.success(response.data()?.login()!!)
    }
}
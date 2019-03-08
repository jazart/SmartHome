package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.apollographql.apollo.api.Input
import com.graphql.SignupMutation
import com.graphql.UserQuery
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import com.jazart.smarthome.util.Status
import javax.inject.Inject


class FetchUserUseCase @Inject constructor(
    private val service: SmartHomeService,
    private val prefs: SharedPreferences
) {

    @Throws(Exception::class)
    suspend fun getUserInfo(): Result<UserQuery.User> {
        val response = prefs.getString("username", "")?.let {  username ->
            service.getUserInfo(username)
        } ?: throw Exception()
        if (response.hasErrors()) {
            return Result.failure(ErrorType.from(response.errors()))
        } else if (response.data() != null) {
            return Result.success(response.data()!!.user())
        }
        return Result.failure(Error.NULL_RESPONSE_VALUE)
    }

    suspend fun signup(username: String, password: String, name: String): Result<String> {
        val response = service.signin(SignupMutation.builder().run {
            nameInput(Input.fromNullable(name))
            passInput(Input.fromNullable(password))
            usernameInput(Input.fromNullable(username))
            build()
        })

        if (response.hasErrors()) {
            return Result.failure(ErrorType.from(response.errors()))
        }
        prefs.edit().putString("jwt", response.data()?.signup() ?: "").apply()
        prefs.edit().putString("username", username).apply()
        return Result.success(response.data()?.signup()!!)
    }
}
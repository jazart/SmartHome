package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.apollographql.apollo.api.Input
import com.graphql.LoginMutation
import com.graphql.SignupMutation
import com.graphql.UserQuery
import com.graphql.type.Credential
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

    suspend fun signIn(username: String, password: String): Result<String> {
        val response = service.signin(LoginMutation.builder().run {
            nameInput(Input.fromNullable(username))
            passInput(Input.fromNullable(password))
            build()
        })

        if (response.hasErrors()) {
            return Result.failure(ErrorType.from(response.errors()))
        }
        prefs.edit().putString("jwt", response.data()?.login() ?: "").apply()
        prefs.edit().putString("username", username).apply()
        return Result.success(response.data()?.login()!!)
    }
}
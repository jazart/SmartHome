package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.apollographql.apollo.api.Input
import com.graphql.SignupMutation
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject


class FetchUserUseCase @Inject constructor(private val service: SmartHomeService) {

    suspend fun getUserInfo(username: String, password: String, name: String): Result {
        service.getUserInfo().data()?.user()?.let {
            return Result.Success(it)
        }
        val response = service.signin(SignupMutation.builder().run {
            nameInput(Input.fromNullable(name))
            passInput(Input.fromNullable(password))
            usernameInput(Input.fromNullable(username))
            build()
        })
        if(response.hasErrors()) {
            return Result.Failure(ErrorType.from(response.errors()))
        }
        return Result.Failure(Error.NULL_RESPONSE_VALUE)
    }

    suspend fun signup(username: String, password: String, name: String): Result {
        val response = service.signin(SignupMutation.builder().run {
            nameInput(Input.fromNullable(name))
            passInput(Input.fromNullable(password))
            usernameInput(Input.fromNullable(username))
            build()
        })

        if(response.hasErrors()) {
            return Result.Failure(ErrorType.from(response.errors()))
        }
//        prefs.edit().putString("user_jwt", response.data()?.signup() ?: "").apply()
        return Result.Success(response.data()?.signup())
    }
}
package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.apollographql.apollo.api.Input
import com.graphql.SignupMutation
import com.graphql.type.Credential
import com.graphql.type.Personal
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val service: SmartHomeService,
    prefs: SharedPreferences) : AuthenticationUseCase(prefs) {

    suspend fun signupUser(credential: Credential, personal: Personal): Result<String?> {
        val response = service.signup(
            SignupMutation(
                Input.fromNullable(credential),
                Input.fromNullable(personal)
            )
        )
        if (response.hasErrors()) {
            return Result.failure(ErrorType.from(response.errors()))
        }
        addToPrefs(response.data()!!.signup()!!, credential.username())
        return if (response.data() == null) Result.failure(Error.NULL_RESPONSE_VALUE) else Result.success(
            response.data()!!.signup()
        )
    }
}
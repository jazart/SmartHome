package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.graphql.UserQuery
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject


class FetchUserUseCase @Inject constructor(private val service: SmartHomeService, private val prefs: SharedPreferences) {

    suspend fun getUserInfo(): Result<UserQuery.User> {
        val response = prefs.getString("username", "")?.let { username ->
            service.getUserInfo(username)
        } ?: return Result.failure(Error.NOT_FOUND)
        return when {
            response.hasErrors() -> Result.failure(ErrorType.from(response.errors()))
            response.data() != null -> Result.success(response.data()?.user()!!)
            else -> Result.failure(Error.NULL_RESPONSE_VALUE)
        }
    }
}
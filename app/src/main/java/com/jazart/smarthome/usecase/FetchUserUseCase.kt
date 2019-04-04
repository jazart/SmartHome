package com.jazart.smarthome.usecase

import android.content.SharedPreferences
import com.graphql.UserQuery
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject


class FetchUserUseCase @Inject constructor(
    private val service: SmartHomeService,
    private val prefs: SharedPreferences
) {

    @Throws(Exception::class)
    suspend fun getUserInfo(): Result<UserQuery.User> {
        val response = prefs.getString("username", "")?.let { username ->
            service.getUserInfo(username)
        } ?: return Result.failure(Error.NOT_FOUND)
        if (response.hasErrors()) {
            return Result.failure(ErrorType.from(response.errors()))
        } else if (response.data() != null) {
            response.data()?.user()?.let {
                return Result.success(it)
            }
        }
        return Result.failure(Error.NULL_RESPONSE_VALUE)
    }
}
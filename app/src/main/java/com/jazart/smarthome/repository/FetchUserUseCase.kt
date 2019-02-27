package com.jazart.smarthome.repository

import com.jazart.smarthome.Error
import com.jazart.smarthome.Result
import com.jazart.smarthome.network.SmartHomeService
import javax.inject.Inject


class FetchUserUseCase @Inject constructor(private val service: SmartHomeService) {

    suspend fun getUserInfo(): Result {
        service.getUserInfo().data()?.user()?.let {
            return Result.Success(it)
        }
        return Result.Failure(Error.NULL_RESPONSE_VALUE)
    }
}
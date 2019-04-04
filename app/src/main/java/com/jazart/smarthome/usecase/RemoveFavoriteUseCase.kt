package com.jazart.smarthome.usecase

import com.graphql.RemoveFavoriteMutation
import com.graphql.type.DeviceInfo
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(private val service: SmartHomeService) {

    suspend fun removeFavorite(deviceInfo: DeviceInfo): Result<String> {
        val response = service.removeFavorite(RemoveFavoriteMutation(deviceInfo))
            ?: return Result.failure(Error.NETWORK_TIMEOUT)

        return when {
            response.hasErrors() -> Result.failure(ErrorType.from(response.errors()))
            response.data()?.removeFavorite() == null -> Result.failure(Error.NULL_RESPONSE_VALUE)
            else -> Result.completed()
        }
    }
}
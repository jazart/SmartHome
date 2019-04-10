package com.jazart.smarthome.usecase

import com.graphql.AddFavoriteMutation
import com.graphql.type.DeviceInfo
import com.jazart.smarthome.network.ImageService
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class FavoriteDeviceUseCase @Inject constructor(val service: SmartHomeService, val imageService: ImageService) {

    suspend fun addFavorite(deviceInfo: DeviceInfo): Result<String> {
        val link = imageService.getImage("JaZart") ?: return Result.failure(Error.NULL_RESPONSE_VALUE)

        val response =
            service.addFavorite(AddFavoriteMutation(deviceInfo)) ?: return Result.failure(Error.NETWORK_TIMEOUT)
        return when {
            response.hasErrors() -> Result.failure(ErrorType.from(response.errors()))
            response.data() == null -> Result.failure(Error.NULL_RESPONSE_VALUE)
            else -> Result.success(link)
        }
    }
}
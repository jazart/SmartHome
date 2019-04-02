package com.jazart.smarthome.usecase

import com.graphql.type.DeviceInfo
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class FavoriteDeviceUseCase @Inject constructor(val service: SmartHomeService) {

    suspend fun addFavorite(deviceInfo: DeviceInfo): Result<String> {

        return Result.success("Success")
    }
}
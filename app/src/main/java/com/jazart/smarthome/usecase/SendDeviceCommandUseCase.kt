package com.jazart.smarthome.usecase

import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.network.ImageService
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class SendDeviceCommandUseCase @Inject constructor(private val service: SmartHomeService, val imageService: ImageService) {

    suspend fun sendCommand(deviceInfo: DeviceInfo, deviceType: DeviceType, command: Command): Result<String> {
        val result = service.sendDeviceCommand(deviceInfo, deviceType, command)
            ?: return Result.failure(Error.NOT_FOUND)
        val imageUri
                = imageService.getImage(deviceInfo.username()) ?: return Result.failure(Error.NULL_RESPONSE_VALUE)
        return when {
            result.hasErrors() -> Result.failure(Error.INVALID_REQUEST)
            else -> Result.success(imageUri)
        }
    }
}
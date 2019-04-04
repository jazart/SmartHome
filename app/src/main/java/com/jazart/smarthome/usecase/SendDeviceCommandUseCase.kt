package com.jazart.smarthome.usecase

import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class SendDeviceCommandUseCase @Inject constructor(private val service: SmartHomeService) {

    suspend fun sendCommand(
        deviceInfo: DeviceInfo,
        deviceType: DeviceType,
        command: Command
    ): Result<String> {
        val result = service.sendDeviceCommand(deviceInfo, deviceType, command)
            ?: return Result.failure(Error.NOT_FOUND)
        return if (result.hasErrors()) Result.failure(Error.INVALID_REQUEST) else Result.completed()
    }
}
package com.jazart.smarthome.usecase

import com.graphql.type.Command
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.Result
import com.jazart.smarthome.network.SmartHomeService
import javax.inject.Inject

class SendDeviceCommandUseCase @Inject constructor(private val service: SmartHomeService) {
    suspend fun sendCommand(uId: String, deviceName: String, command: Command): Result<String> {
        return try {
            val result = service.sendDeviceCommand(uId, deviceName, command)
            if(result.hasErrors()) Result.failure(Error.INVALID_REQUEST) else Result.completed()
        } catch (e: Exception) {
            Result.failure(Error.INVALID_REQUEST)
        }
    }
}
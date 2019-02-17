package com.jazart.smarthome

import com.graphql.type.Command
import com.jazart.smarthome.network.SmartHomeService
import javax.inject.Inject

class SendDeviceCommandUseCase @Inject constructor(private val service: SmartHomeService) {
    suspend fun sendCommand(uId: String, deviceName: String, command: Command): Result {
        return try {
            val result = service.sendDeviceCommand(uId, deviceName, command)
            if(result.hasErrors()) Result.Failure(Error.INVALID_REQUEST) else Result.Completed
        } catch (e: Exception) {
            Result.Failure(Error.INVALID_REQUEST)
        }
    }
}
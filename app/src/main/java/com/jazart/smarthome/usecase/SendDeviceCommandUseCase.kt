package com.jazart.smarthome.usecase

import com.graphql.StreamCommandMutation
import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.network.ImageService
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class SendDeviceCommandUseCase @Inject constructor(private val service: SmartHomeService, val imageService: ImageService) {

    suspend fun sendCommand(deviceInfo: DeviceInfo, deviceType: DeviceType, command: Command): Result<String> {

        if(deviceType == DeviceType.LIGHT) {
            val result = service.sendDeviceCommand(deviceInfo, deviceType, command)
                ?: return Result.failure(Error.UNKNOWN_ERROR)
            return if(result.hasErrors()) Result.failure(ErrorType.from(result.errors())) else Result.completed()
        }
        if(command == Command.STREAM) {
            val result = service.startStream(StreamCommandMutation
                .builder()
                .deviceInfo(deviceInfo).command(command).build()
            ) ?: return Result.failure(Error.NOT_FOUND)
            delay(1500L)
            return if (result.hasErrors()) Result.failure(Error.INVALID_REQUEST) else Result.success(result.data()?.startStreamCommand()!!)

        } else if(command == Command.SNAP) {
            service.sendDeviceCommand(deviceInfo, deviceType, command)
                ?: return Result.failure(Error.UNKNOWN_ERROR)
            delay(1000L)
            val imageUri =
                imageService.getImage(deviceInfo.username()) ?: return Result.failure(Error.NULL_RESPONSE_VALUE)
            return Result.success(imageUri)
            }
        return Result.failure(Error.UNKNOWN_ERROR)
    }
}
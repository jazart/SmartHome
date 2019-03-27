package com.jazart.smarthome.usecase

import com.graphql.AddDeviceMutation
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class AddDeviceUseCase @Inject constructor(val service: SmartHomeService) {

    suspend fun addDevice(deviceInfo: DeviceInfo, deviceType: DeviceType): Result<String> {
        val response =
            service.addDevice(AddDeviceMutation(deviceInfo, deviceType)) ?: return Result.failure(Error.NETWORK_TIMEOUT)
        return when {
            response.hasErrors() -> Result.failure(ErrorType.from(response.errors()))
            response.data()?.addDevice() != null -> Result.success(response.data()!!.addDevice()!!)
            else -> Result.failure(Error.NULL_RESPONSE_VALUE)
        }
    }
}
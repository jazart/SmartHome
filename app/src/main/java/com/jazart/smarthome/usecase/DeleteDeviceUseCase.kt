package com.jazart.smarthome.usecase

import com.graphql.DeleteDeviceMutation
import com.graphql.type.DeviceInfo
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class DeleteDeviceUseCase @Inject constructor(private val smartHomeService: SmartHomeService) {

    suspend fun deleteDevice(deviceInfo: DeviceInfo): Result<String> {
        val response = smartHomeService.deleteDevice(
            DeleteDeviceMutation.builder().deviceInfo(deviceInfo).build()
        ) ?: return Result.failure(Error.NETWORK_TIMEOUT)
        return when {
            response.data()?.removeDevice() == deviceInfo.deviceName() -> Result.success(deviceInfo.deviceName())
            else -> Result.failure(Error.NOT_FOUND)
        }
    }
}
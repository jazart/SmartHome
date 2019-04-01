package com.jazart.smarthome.usecase

import com.graphql.DeleteDeviceMutation
import com.graphql.UserQuery
import com.graphql.type.DeviceInfo
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Error
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class DeleteDeviceUseCase @Inject constructor(private val smartHomeService: SmartHomeService) {

    suspend fun deleteDevice(device: UserQuery.Device): Result<String> {
        val response = smartHomeService.deleteDevice(
            DeleteDeviceMutation.builder().deviceInfo(
                DeviceInfo.builder().username(device.owner()).deviceName(device.name()).build()

            ).build()
        ) ?: return Result.failure(Error.NETWORK_TIMEOUT)
        if (response.data()?.removeDevice() == device.name()) {
            return Result.success(device.name())
        }
        return Result.failure(Error.NOT_FOUND)
    }
}
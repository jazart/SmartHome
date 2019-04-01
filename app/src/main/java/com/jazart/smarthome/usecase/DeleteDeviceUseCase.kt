package com.jazart.smarthome.usecase

import com.graphql.DeleteDeviceMutation
import com.graphql.UserQuery
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class DeleteDeviceUseCase @Inject constructor(private val smartHomeService: SmartHomeService) {

    suspend fun deleteDevice(device: UserQuery.Device): Result<String>{
        val response = smartHomeService.deleteDevice(DeleteDeviceMutation.builder().deviceInfo(

        ))
        return Result.success("Woo!")
    }
}
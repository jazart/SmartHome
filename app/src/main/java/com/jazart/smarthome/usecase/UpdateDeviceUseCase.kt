package com.jazart.smarthome.usecase

import com.jazart.smarthome.network.SmartHomeService
import javax.inject.Inject

class UpdateDeviceUseCase @Inject constructor(val smartHomeService: SmartHomeService) {

    /**
     * TODO: build GraphQL mutation, send to service and await result. Return result wrapper based on the returned response.
     */
//    fun updateDevice(): Result {
//        withContext(Dispatchers.IO) {
//            val callResult = smartHomeService.updateDevice()
//        }
//        return Result.Success(Event("ok"))
//    }
}
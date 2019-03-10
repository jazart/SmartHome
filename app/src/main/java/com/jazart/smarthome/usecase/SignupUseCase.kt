package com.jazart.smarthome.usecase

import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.util.Result
import javax.inject.Inject

class SignupUseCase @Inject constructor(service: SmartHomeService) {
    fun signupUser(info: List<String>): Result<String> {

        return Result.success("Woot!")
    }
}
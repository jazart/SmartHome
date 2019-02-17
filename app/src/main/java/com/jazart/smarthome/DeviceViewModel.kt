package com.jazart.smarthome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeviceViewModel @Inject constructor(private val sendDeviceCommandUseCase: SendDeviceCommandUseCase) : ViewModel(),
    CoroutineScope {

    override val coroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()

    private val _currentDevice = MutableLiveData<UserQuery.Device>()
    val currentDevice: LiveData<UserQuery.Device>
        get() = _currentDevice

    fun initCurrentDevice(device: UserQuery.Device) {
        _currentDevice.value = device
    }

    infix fun sendCommand(command: Command) {
        launch {
            _currentDevice.value?.let { device ->
                val res = sendDeviceCommandUseCase.sendCommand(device.name(), device.name(), command)

            }
        }
    }
}
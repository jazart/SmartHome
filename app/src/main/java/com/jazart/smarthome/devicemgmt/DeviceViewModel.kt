package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.jazart.smarthome.usecase.SendDeviceCommandUseCase
import com.tinder.scarlet.Event
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

    private val _removeEvent = MutableLiveData<com.jazart.smarthome.util.Event<String>>()
    val removeEvent: LiveData<com.jazart.smarthome.util.Event<String>>
        get() = _removeEvent

    private val _removeError = MutableLiveData<com.jazart.smarthome.util.Event<String>>()
    val removeError: LiveData<com.jazart.smarthome.util.Event<String>>
        get() = _removeError



   fun removeDevice(id: Long){


    }
}
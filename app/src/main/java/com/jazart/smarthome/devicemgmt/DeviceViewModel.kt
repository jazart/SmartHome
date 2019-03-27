package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.usecase.SendDeviceCommandUseCase
import com.jazart.smarthome.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeviceViewModel @Inject constructor(private val sendDeviceCommandUseCase: SendDeviceCommandUseCase) : ViewModel(),
    CoroutineScope {

    override val coroutineContext
        get() = Dispatchers.Main + job
    private val job = SupervisorJob()

    private val _showEditMode = MutableLiveData<Event<Boolean>>()
    val showEditMode: LiveData<Event<Boolean>>
        get() = _showEditMode

    private var isShowing = false

    private val _currentDevice = MutableLiveData<UserQuery.Device>()
    val currentDevice: LiveData<UserQuery.Device>
        get() = _currentDevice


    fun initCurrentDevice(device: UserQuery.Device) {
        _currentDevice.value = device
    }

    infix fun send(command: Command) {
        launch {
            _currentDevice.value?.let { device ->
                val res = sendDeviceCommandUseCase.sendCommand(
                    DeviceInfo.builder().deviceName(device.name()).username(device.owner()).build(),
                    DeviceType.CAMERA, Command.TURN_ON
                )
            }
        }
    }

    fun toggleEdit() {
        isShowing = !isShowing
        _showEditMode.value = Event(isShowing)
    }
}
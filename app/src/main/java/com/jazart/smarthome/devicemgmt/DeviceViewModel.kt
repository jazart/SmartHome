package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.usecase.DeleteDeviceUseCase
import com.jazart.smarthome.usecase.FavoriteDeviceUseCase
import com.jazart.smarthome.usecase.SendDeviceCommandUseCase
import com.jazart.smarthome.util.Event
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject

class DeviceViewModel @Inject constructor(
    private val sendDeviceCommandUseCase: SendDeviceCommandUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
    private val favoriteDeviceUseCase: FavoriteDeviceUseCase
) : ViewModel(),
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

    private val _removeDeviceResult = MutableLiveData<Event<String>>()
    val removeDeviceResult: LiveData<Event<String>>
        get() = _removeDeviceResult

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

    fun deleteDevice(device: UserQuery.Device) {
        launch {
            withContext(Dispatchers.Default) {
                val result = deleteDeviceUseCase.deleteDevice(device)
                when (result.status) {
                    is Status.Success -> _removeDeviceResult.postValue(Event(result.data))
                    is Status.Failure -> return@withContext
                }
            }
        }
    }

    infix fun favorite(device: UserQuery.Device) {
        launch {
            withContext(Dispatchers.Default) {
                val result = favoriteDeviceUseCase.addFavorite(DeviceInfo.builder().run {
                    deviceName(device.name())
                    username(device.owner())
                    command(emptyList())
                    build()
                })

                when (result.status) {
                    is Status.Success -> return@withContext
                    is Status.Failure -> return@withContext
                }
            }
        }
    }
}
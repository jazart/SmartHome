package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.jazart.smarthome.usecase.DeleteDeviceUseCase
import com.jazart.smarthome.usecase.FavoriteDeviceUseCase
import com.jazart.smarthome.usecase.RemoveFavoriteUseCase
import com.jazart.smarthome.usecase.SendDeviceCommandUseCase
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Event
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject

class DeviceViewModel @Inject constructor(
    private val sendDeviceCommandUseCase: SendDeviceCommandUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
    private val favoriteDeviceUseCase: FavoriteDeviceUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
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

    private val _favorite = MutableLiveData<Event<String>>()
    val favorite: LiveData<Event<String>>
        get() = _favorite

    private val _commandValue = MutableLiveData<Event<String>>()
    val commandValue: LiveData<Event<String>>
        get() = _commandValue

    private val _streamValue = MutableLiveData<Event<String>>()
    val streamValue: LiveData<Event<String>>
        get() = _streamValue
    fun initCurrentDevice(device: UserQuery.Device) {
        _currentDevice.value = device
    }

    infix fun send(command: Command) {
        launch {
            withContext(Dispatchers.Default) {
                _currentDevice.value?.let { device ->
                    val res = sendDeviceCommandUseCase.sendCommand(
                        DeviceInfo.builder().deviceName(device.name()).username(device.owner()).type(device.type()).command(
                            device.commands()
                        ).build(),
                        _currentDevice.value!!.type(), command
                    )
                    if (res.data != null && command == Command.STREAM) {
                        _streamValue.postValue(Event(res.data))
                    }
                    if(res.data != null && command == Command.SNAP) {
                        _commandValue.postValue(Event(res.data))
                    }
                }
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
                val result = deleteDeviceUseCase.deleteDevice(buildDeviceInfo(device))
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
                _currentDevice.postValue(device.copy(true))
                if (!device.isFavorite) {
                    val result = favoriteDeviceUseCase.addFavorite(buildDeviceInfo(device))
                    when (result.status) {
                        is Status.Success -> _favorite.postValue(Event(result.data))
                        is Status.Failure -> return@withContext

                        Status.Completed -> return@withContext
                    }
                } else {
                    val result = removeFavoriteUseCase.removeFavorite(buildDeviceInfo(device))
                    when (result.status) {
                        is Status.Completed -> _removeDeviceResult.postValue(Event(result.data))
                        is Status.Failure -> _removeDeviceResult.postValue(Event(ErrorType.from(result.error!!)))
                        Status.Success -> return@withContext
                    }
                }
            }
        }
    }

    private fun buildDeviceInfo(device: UserQuery.Device): DeviceInfo {
        return DeviceInfo.builder().run {
            deviceName(device.name())
            username(device.owner())
            command(emptyList())
            type(device.type())
            build()
        }
    }
}

fun UserQuery.Device.copy(negateFavorite: Boolean = false): UserQuery.Device {
    return UserQuery.Device(
        __typename(), name(), status(), commands(),
        owner(), if (negateFavorite) !isFavorite else isFavorite, type()
    )
}
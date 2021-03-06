package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import com.jazart.smarthome.usecase.AddDeviceUseCase
import com.jazart.smarthome.usecase.FetchUserUseCase
import com.jazart.smarthome.util.ErrorType
import com.jazart.smarthome.util.Event
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val addDeviceUseCase: AddDeviceUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _devices = MutableLiveData<List<UserQuery.Device>>()
    val devices: LiveData<List<UserQuery.Device>> =
        Transformations.map(_devices) { devices ->
            favoriteDevice.value = getFavoriteDeviceIfAvailable()
            devices.filter { !it.isFavorite }.toList()
        }
    private val job = SupervisorJob()

    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    private val _addDeviceResult = MutableLiveData<Event<String>>()
    val addDeviceResult: LiveData<Event<String>>
        get() = _addDeviceResult

    var favoriteDevice = MutableLiveData<UserQuery.Device?>()

    fun loadDevices() {
        getDevicesFromRepo {
            withContext(Dispatchers.Default) {
                val userInfo = fetchUserUseCase.getUserInfo()
                when (userInfo.status) {
                    is Status.Success -> {
                        _devices.postValue((userInfo.data?.devices()))
                        _user.postValue(userInfo.data?.username())
                    }
                }
            }
        }
    }

    fun addDevice(deviceName: String, deviceType: DeviceType) {
        launch {
            withContext(Dispatchers.Default) {
                if (!isInfoValid(deviceName, deviceType)) {
                    _addDeviceResult.postValue(Event("Error adding device, please validate information."))
                    return@withContext
                }
                val result =
                    addDeviceUseCase.addDevice(
                        createDevice(deviceName, deviceType),
                        deviceType
                    )
                when (result.status) {
                    is Status.Success -> _addDeviceResult.postValue(Event(result.data))
                    is Status.Failure -> _addDeviceResult.postValue(Event(result.error?.let { ErrorType.from(it) }))
                    else -> return@withContext
                }
            }
        }
    }

    private fun createDevice(deviceName: String, deviceType: DeviceType): DeviceInfo {
        val commands = when (deviceType) {
            DeviceType.LIGHT -> mutableListOf(Command.PULSE)
            DeviceType.CAMERA -> mutableListOf(Command.STREAM, Command.SNAP)
            else -> mutableListOf()
        }
        commands.addAll(listOf(Command.TURN_ON, Command.TURN_OFF))
        return DeviceInfo.builder().username(user.value!!).deviceName(deviceName).command(commands)
            .isFavorite(false).type(deviceType).build()
    }

    private fun getDevicesFromRepo(block: suspend () -> Unit): Job {
        return launch { block() }
    }

    private fun isInfoValid(deviceName: String, deviceType: DeviceType): Boolean {
        return deviceName.isNotBlank() && deviceType in listOf(
            DeviceType.CAMERA,
            DeviceType.LIGHT,
            DeviceType.MOTION,
            DeviceType.HOME_TEMPERATURE,
            DeviceType.LIGHT
        )
    }

    private fun getFavoriteDeviceIfAvailable(): UserQuery.Device? {
        return _devices.value?.find { it.isFavorite }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }

    companion object {
        fun testDeviceData(): List<UserQuery.Device> {
            return listOf(
                UserQuery.Device(
                    "TV",
                    "Television",
                    com.graphql.type.Status.CONNECTED,
                    listOf(Command.TURN_ON, Command.TURN_ON, Command.TURN_OFF, Command.PULSE),
                    "jeremy",
                    false,
                    DeviceType.TV
                ),
                UserQuery.Device(
                    "Camera",
                    "Motion Sensor",
                    com.graphql.type.Status.CONNECTED,
                    listOf(),
                    "jeremy",
                    false,
                    DeviceType.MOTION
                ),
                UserQuery.Device(
                    "Camera",
                    "Light",
                    com.graphql.type.Status.CONNECTED,
                    listOf(),
                    "jeremy",
                    false,
                    DeviceType.LIGHT
                ),
                UserQuery.Device(
                    "Camera",
                    "Bluetooth Device",
                    com.graphql.type.Status.CONNECTED,
                    listOf(),
                    "jeremy",
                    false,
                    DeviceType.BLUETOOTH_DEVICE
                ),
                UserQuery.Device(
                    "Camera",
                    "Camera",
                    com.graphql.type.Status.CONNECTED,
                    listOf(),
                    "jeremy",
                    false,
                    DeviceType.CAMERA
                ),
                UserQuery.Device(
                    "Camera",
                    "Temperature",
                    com.graphql.type.Status.CONNECTED,
                    listOf(),
                    "jeremy",
                    false,
                    DeviceType.HOME_TEMPERATURE
                )
            )
        }
    }
}
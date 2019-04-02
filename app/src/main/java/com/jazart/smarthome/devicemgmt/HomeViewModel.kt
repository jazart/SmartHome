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
            favoriteDevice = getFavoriteIfAvaliable();
            devices.filter { !it.isFavorite }.toList()
        }
    private val job = Job()

    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    private val _addDeviceResult = MutableLiveData<Event<String>>()
    val addDeviceResult: LiveData<Event<String>>
        get() = _addDeviceResult

    var favoriteDevice = getFavoriteIfAvaliable()

    fun loadDevices() {
        getDevicesFromRepo {
            val userInfo = fetchUserUseCase.getUserInfo()
            when (userInfo.status) {
                is Status.Success -> {
                    _devices.postValue((userInfo.data?.devices()))
                    _user.postValue(userInfo.data?.username())
                }
            }
        }
    }

    fun addDevice(deviceName: String, deviceType: DeviceType, isFavorite: Boolean) {
        launch {
            withContext(Dispatchers.Default) {
                if (!isInfoValid(deviceName, deviceType)) {
                    _addDeviceResult.postValue(Event("Error adding device, please validate information."))
                    return@withContext
                }
                val result =
                    addDeviceUseCase.addDevice(
                        DeviceInfo.builder().username(user.value!!).deviceName(deviceName).command(listOf())
                            .isFavorite(false).build(),
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

    private fun getDevicesFromRepo(block: suspend () -> Unit): Job {
        return launch { block() }
    }

    private fun isInfoValid(deviceName: String, deviceType: DeviceType): Boolean {
        return deviceName.isNotBlank() && deviceType in listOf(DeviceType.CAMERA, DeviceType.LIGHT)
    }

    private fun getFavoriteIfAvaliable(): UserQuery.Device? {
        return _devices.value?.find { it.isFavorite }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }

    private fun testDeviceData(): List<UserQuery.Device> {
        return listOf(
            UserQuery.Device(
                "TV",
                "Television",
                com.graphql.type.Status.CONNECTED,
                listOf(Command.TURN_ON, Command.TURN_ON, Command.TURN_OFF, Command.PULSE),
                "jeremy",
                false
            ),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy", false)
        )
    }
}
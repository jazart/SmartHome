package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.jazart.smarthome.usecase.FetchUserUseCase
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _devices = MutableLiveData<List<UserQuery.Device>>()
    val devices: LiveData<List<UserQuery.Device>> = Transformations.map(_devices) { it.toList() }
    private val job = Job()

    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    fun loadDevices() {
        getDevicesFromRepo {
            val userInfo = fetchUserUseCase.getUserInfo()
            when (userInfo.status) {
                is Status.Success -> {
                    _devices.postValue((userInfo.data?.devices()))
                    _user.postValue(userInfo.data?.username())
                }
            }
            _devices.postValue(
                listOf(
                    UserQuery.Device(
                        "TV",
                        "Television",
                        com.graphql.type.Status.CONNECTED,
                        listOf(Command.TURN_ON, Command.TURN_ON, Command.TURN_OFF, Command.PULSE),
                        "jeremy"
                    ),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
                    UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy")
                )
            )
        }
    }

    private val _currentDevice = MutableLiveData<UserQuery.Device>()
    val currentDevice: LiveData<UserQuery.Device>
        get() = _currentDevice

    fun updateCurrentDevice(pos: Int) {
        _currentDevice.value = devices.value?.get(pos)
    }

    fun addDevice() {
    }

    private fun getDevicesFromRepo(block: suspend () -> Unit): Job {
        return launch { block() }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}
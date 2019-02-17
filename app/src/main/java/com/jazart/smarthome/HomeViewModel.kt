package com.jazart.smarthome

import androidx.lifecycle.*
import com.graphql.UserQuery
import com.graphql.type.Command
import com.jazart.smarthome.repository.FetchUserUseCase
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(private val fetchUserUseCase: FetchUserUseCase,
                                        private val sendDeviceCommandUseCase: SendDeviceCommandUseCase) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val _devices = MutableLiveData<List<UserQuery.Device>>()
    val devices: LiveData<List<UserQuery.Device>> = Transformations.map(_devices) { it.toList() }
    private val job = Job()

    fun loadDevices() {
        getDevicesFromRepo {
            val userInfo = fetchUserUseCase.getUserInfo()
            when(userInfo) {
                is Result.Success<*> -> _devices.postValue((userInfo.data as UserQuery.User).devices())
            }
        }
    }

    private fun getDevicesFromRepo(block: suspend () -> Unit): Job {
        return launch { block() }
    }

    private val _currentDevice = MutableLiveData<UserQuery.Device>()
    val currentDevice: LiveData<UserQuery.Device>
        get() = _currentDevice

    fun updateCurrentDevice(pos: Int) {
        _currentDevice.value = devices.value?.get(pos)
    }

    infix fun sendCommand(command: Command) {
        launch {
            _currentDevice.value?.let { device ->
                val res = sendDeviceCommandUseCase.sendCommand(device.name(), device.name(), command)
            }
        }
    }

    fun reloadDevice(offset: Int) {

    }

    fun addDevice() {

    }

    fun deviceResults() {

    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }


}
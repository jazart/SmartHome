package com.jazart.smarthome.devicemgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.graphql.type.Command
import com.jazart.smarthome.usecase.FetchUserUseCase
import com.jazart.smarthome.usecase.SendDeviceCommandUseCase
import com.jazart.smarthome.util.Event
import com.jazart.smarthome.util.Result
import com.jazart.smarthome.util.Status
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val sendDeviceCommandUseCase: SendDeviceCommandUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _devices = MutableLiveData<List<UserQuery.Device>>()
    val devices: LiveData<List<UserQuery.Device>> = Transformations.map(_devices) { it.toList() }
    private val job = Job()
    private val _showEditMode = MutableLiveData<Event<Boolean>>()
    val showEditMode: LiveData<Event<Boolean>>
        get() = _showEditMode

    private val _bottomFabClicked = MutableLiveData<Event<Int>>()
    val bottomFabClicked: LiveData<Event<Int>>
        get() = _bottomFabClicked

    private val _user = MutableLiveData<String>()
    val user: LiveData<String>
        get() = _user

    private var isShowing = false

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

    private val _currentDevice = MutableLiveData<UserQuery.Device>()
    val currentDevice: LiveData<UserQuery.Device>
        get() = _currentDevice

    fun updateCurrentDevice(pos: Int) {
        _currentDevice.value = devices.value?.get(pos)
    }

    fun toggleEdit() {
        isShowing = !isShowing
        _showEditMode.value = Event(isShowing)
    }

    infix fun sendCommand(command: Command) {
        launch {
//            _currentDevice.value?.let { device ->
//                val res = sendDeviceCommandUseCase.sendCommand(device.name(), device.name(), command)
//            }
            sendDeviceCommandUseCase.sendCommand("", "", command)
        }
    }

    fun onBottomFabClicked(destination: Int) {
        _bottomFabClicked.value = Event(destination)
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
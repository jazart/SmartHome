package com.jazart.smarthome

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.graphql.UserQuery
import com.jazart.smarthome.repository.UserRepo
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(val repo: UserRepo) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val _devices = repo.devices
    val devices: LiveData<List<UserQuery.Device>> = Transformations.map(_devices) { it.toList() }
    private val job = Job()

    fun loadDevices() {
        getDevicesFromRepo { repo.awaitUserCall() }
    }

    private fun getDevicesFromRepo(block: suspend () -> Unit): Job {
        return launch { block() }
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
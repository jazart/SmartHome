package com.jazart.smarthome

import androidx.lifecycle.ViewModel
import com.jazart.smarthome.repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class DeviceViewModel @Inject constructor(val repo: UserRepo) : ViewModel(), CoroutineScope {

    override val coroutineContext
        get() = Dispatchers.Main + job
    private val job = Job()
}
package com.jazart.smarthome.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jazart.smarthome.util.Event
import javax.inject.Inject

class FabViewModel @Inject constructor() : ViewModel() {

    private val _bottomFabClicked = MutableLiveData<Event<Int>>()
    val bottomFabClicked: LiveData<Event<Int>>
        get() = _bottomFabClicked

    private val _deleteIconClicked = MutableLiveData<Event<Int>>()
    val deleteIconClicked: LiveData<Event<Int>>
        get() = _deleteIconClicked

    fun onBottomFabClicked(destination: Int) {
        _bottomFabClicked.value = Event(destination)
    }

    fun onMenuClicked(): Boolean {
        _deleteIconClicked.value = Event(0)
        return true
    }
}
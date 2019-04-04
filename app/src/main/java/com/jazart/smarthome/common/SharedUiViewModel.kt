package com.jazart.smarthome.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jazart.smarthome.util.Event
import javax.inject.Inject

class SharedUiViewModel @Inject constructor() : ViewModel() {

    private val _bottomFabClicked = MutableLiveData<Event<Int>>()
    val bottomFabClicked: LiveData<Event<Int>>
        get() = _bottomFabClicked

    private val _iconClicked = MutableLiveData<Event<Int>>()
    val iconClicked: LiveData<Event<Int>>
        get() = _iconClicked

//    private val _highlightIcon = MutableLiveData<Event<Int>>()
    val highlightIcon = MutableLiveData<Event<Int>>()
//        get() = _highlightIcon

    fun onBottomFabClicked(destination: Int) {
        _bottomFabClicked.value = Event(destination)
    }

    fun onMenuClicked(menuId: Int): Boolean {
        _iconClicked.value = Event(menuId)
        return true
    }

}
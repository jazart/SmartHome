package com.jazart.smarthome.models

data class Device(val name: String,
                  val status: Status)


sealed class Status {
    object On : Status()
    object Off : Status()
}

package com.jazart.smarthome.models

data class Device(val name: String,
                  val status: Status,
                  val commands: List<Command> = listOf())


sealed class Status {
    object Connected : Status()
    object Disconnected : Status()

    override fun toString(): String = when(this) {
        is Status.Connected -> "Connected"
        is Status.Disconnected -> "Disconnected"
    }
}

sealed class Command {
    object Pulse : Command()
    object Turn_On : Command()
    object Turn_Off : Command()
}

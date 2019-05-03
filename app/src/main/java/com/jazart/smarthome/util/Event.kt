package com.jazart.smarthome.util

import java.util.concurrent.atomic.AtomicBoolean

class Event<out T>(private val consumable: T? = null) {
    private val isConsumed = AtomicBoolean(false)

    fun consume(): T? {
        return if (isConsumed.compareAndSet(false, true)) {
            consumable
        } else {
            null
        }
    }

    fun peek(): T? = consumable
}
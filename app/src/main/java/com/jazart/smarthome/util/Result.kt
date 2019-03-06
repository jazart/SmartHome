package com.jazart.smarthome.util

sealed class Result {
    class Success<out T>(val data: T) : Result()
    class Failure(val error: Error, val exception: Exception = Exception()) : Result()
    object Completed : Result()

}

enum class Error(message: String = "") {
    NETWORK_TIMEOUT,
    INVALID_REQUEST,
    INVALID_INPUT,
    INVALID_ACCESS,
    NULL_RESPONSE_VALUE
}
package com.jazart.smarthome.util

data class Result<out T>(val data: T?, val error: Error?, val status: Status)  {
    companion object {
        fun <T> success(data: T): Result<T> = Result(data, null, Status.Success)
        fun <T> failure(error: Error): Result<T> = Result(null, error, Status.Failure)
        fun <T> completed(): Result<T> = Result(null, null, Status.Completed)
    }
}

enum class Error(val message: String = "") {
    NETWORK_TIMEOUT,
    INVALID_REQUEST,
    INVALID_INPUT,
    INVALID_ACCESS,
    NULL_RESPONSE_VALUE,
    INVALID_CREDENTIALS,
    NOT_FOUND,
    UNKNOWN_ERROR,
    DUP_EMAIL,
    DUP_USERNAME,
    INVALID_PASSWORD
}

sealed class Status {
    object Success : Status()
    object Failure : Status()
    object Completed : Status()
}
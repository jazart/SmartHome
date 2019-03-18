package com.jazart.smarthome.util

object ErrorType {

    fun from(errors: List<com.apollographql.apollo.api.Error>): Error {
        val errorMessage = errors[0].message() ?: return Error.UNKNOWN_ERROR
        return when {
            errorMessage.contains("Unauthorized access") -> Error.INVALID_ACCESS
            errorMessage.contains("Invalid username/password") -> Error.INVALID_CREDENTIALS
            errorMessage.contains("Bad input") -> Error.INVALID_INPUT
            errorMessage.contains("User already signed up") -> Error.DUP_USERNAME
            errorMessage == "Email is taken" -> Error.DUP_EMAIL
            else -> Error.NULL_RESPONSE_VALUE
        }
    }

    fun from(error: Error): String {
        return when (error) {
            Error.NETWORK_TIMEOUT -> "The network is taking a while to respond. Please try again later."
            Error.INVALID_REQUEST -> "Invalid request. Please retry."
            Error.INVALID_INPUT -> "Please ensure your input is valid."
            Error.INVALID_ACCESS -> "You're not allowed to do that."
            Error.NULL_RESPONSE_VALUE -> "The network is busy at this time. Please try again later."
            Error.INVALID_CREDENTIALS -> "Invalid username/password."
            Error.NOT_FOUND -> "We couldn't find what you were looking for."
            Error.UNKNOWN_ERROR -> "Oops, something went wrong."
            Error.DUP_EMAIL -> "That email is already taken."
            Error.DUP_USERNAME -> "That username is already taken."
            Error.INVALID_PASSWORD -> "-Password must be at least 8 characters\n" +
                    " -Password must contain alphanumeric characters"
        }
    }
}
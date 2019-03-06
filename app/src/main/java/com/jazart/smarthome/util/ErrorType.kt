package com.jazart.smarthome.util

import com.apollographql.apollo.api.Error

object ErrorType {
    // TODO: Parse list of response errors and return the appropriate error enum type.
    fun from(errors: List<Error>): com.jazart.smarthome.util.Error = com.jazart.smarthome.util.Error.INVALID_REQUEST
}
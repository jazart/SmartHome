package com.jazart.smarthome.util

import com.apollographql.apollo.api.Error

object ErrorType {
    // TODO: Parse list of response errors and return the appropriate error enum type.
    fun from(errors: List<Error>): com.jazart.smarthome.util.Error {
        if (errors[0].message()?.contains("Unauthorized access")!!) {
            return com.jazart.smarthome.util.Error.INVALID_ACCESS
        }
        else if (errors[0].message()?.contains("Network timeout")!!) {
            return com.jazart.smarthome.util.Error.NETWORK_TIMEOUT
        }
        else if (errors[0].message()?.contains("Bad input")!!) {
            return com.jazart.smarthome.util.Error.INVALID_INPUT
        }
        else if (errors[0].message()?.contains("Bad credentials")!!) {
            return com.jazart.smarthome.util.Error.INVALID_REQUEST
        }
        else if(errors[0].message()?.contains("Missing value")!!) {
            return com.jazart.smarthome.util.Error.NULL_RESPONSE_VALUE
        }
        else
        {
            return com.jazart.smarthome.util.Error.UNKNOWN_ERROR //for error that may not be checked for
        }
    }
}
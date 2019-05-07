package com.jazart.smarthome.network

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.graphql.*
import com.graphql.type.Command
import com.graphql.type.DeviceInfo
import com.graphql.type.DeviceType
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SmartHomeService @Inject constructor(private var apolloClient: ApolloClient) {

    suspend fun sendDeviceCommand(
        deviceInfo: DeviceInfo,
        deviceType: DeviceType,
        command: Command
    ): Response<UpdateDeviceMutation.Data>? {
        return makeCall {
            apolloClient.mutate(
                UpdateDeviceMutation(deviceInfo, deviceType, command)
            ).await()
        }
    }

    suspend fun getUserInfo(username: String): Response<UserQuery.Data>? {
        return makeCall {
            apolloClient.query(UserQuery(username)).await()
        }
    }

    suspend fun signin(mutation: LoginMutation): Response<LoginMutation.Data>? {
        return makeCall {
            apolloClient.mutate(mutation).await()
        }
    }

    suspend fun signup(mutation: SignupMutation): Response<SignupMutation.Data>? {
        return makeCall {
            apolloClient.mutate(mutation).await()
        }
    }

    suspend fun updateDevice(mutation: UpdateDeviceMutation): Response<*>? {
        return makeCall {
            apolloClient.mutate(mutation).await()
        }
    }

    private inline fun <T> makeCall(block: () -> Response<T>): Response<T>? {
        return try {
            block()
        } catch (e: ApolloException) {
            return null
        }
    }

    suspend fun addDevice(addDeviceMutation: AddDeviceMutation): Response<AddDeviceMutation.Data>? {
        return makeCall {
            apolloClient.mutate(addDeviceMutation).await()
        }
    }

    suspend fun deleteDevice(deleteDeviceMutation: DeleteDeviceMutation): Response<DeleteDeviceMutation.Data>? {
        return makeCall {
            apolloClient.mutate(deleteDeviceMutation).await()
        }
    }

    suspend fun addFavorite(addFavoriteMutation: AddFavoriteMutation): Response<AddFavoriteMutation.Data>? {
        return makeCall {
            apolloClient.mutate(addFavoriteMutation).await()
        }
    }

    suspend fun removeFavorite(removeFavoriteMutation: RemoveFavoriteMutation): Response<RemoveFavoriteMutation.Data>? {
        return makeCall {
            apolloClient.mutate(removeFavoriteMutation).await()
        }
    }

    suspend fun startStream(streamCommandMutation: StreamCommandMutation): Response<StreamCommandMutation.Data>? {
        return makeCall {
            apolloClient.mutate(streamCommandMutation).await()
        }
    }
    companion object {
        const val BASE_URL = "http://smarthomeserver.us-west-2.elasticbeanstalk.com/graphql"
        const val BASE_URL_DEV = "http://1bf130dd.ngrok.io/graphql"
    }

}

suspend fun <T> ApolloCall<T>.await(): Response<T> {
    return suspendCoroutine { cont ->
        enqueue(object : ApolloCall.Callback<T>() {
            override fun onResponse(response: Response<T>) {
                cont.resume(response)
            }

            override fun onFailure(e: ApolloException) {
                cont.resumeWithException(e)
            }

            override fun onNetworkError(e: ApolloNetworkException) {
                e.printStackTrace()
            }
        })
    }
}
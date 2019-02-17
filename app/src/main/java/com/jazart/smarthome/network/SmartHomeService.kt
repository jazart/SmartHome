package com.jazart.smarthome.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.graphql.UpdateDeviceMutation
import com.graphql.UserQuery
import com.graphql.type.Command
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SmartHomeService @Inject constructor(var apolloClient: ApolloClient) {

    suspend fun sendDeviceCommand(uId: String, deviceName: String, command: Command): Response<*> {
        return apolloClient.mutate(
            UpdateDeviceMutation(
                Input.fromNullable(uId),
                Input.fromNullable(deviceName),
                Input.fromNullable(command)
            )
        ).await()
    }

    suspend fun getUserInfo(): Response<UserQuery.Data>{
        return apolloClient.query(UserQuery()).await()
    }

    companion object {
        const val BASE_URL = "http://smarthome-jazart.us-west-2.elasticbeanstalk.com/graphql"
    }

}

class SmarthomeTokenManager : TokenManager {
    override fun token(): Token = ""
    override fun newToken(): Token = ""
}

interface TokenManager {
    fun token(): Token
    fun newToken(): Token
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
        })
    }
}

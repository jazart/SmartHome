package com.jazart.smarthome.network

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.graphql.LoginMutation
import com.graphql.SignupMutation
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

    suspend fun getUserInfo(username: String): Response<UserQuery.Data> {
        return apolloClient.query(UserQuery(username)).await()
    }

    suspend fun signin(mutation: LoginMutation): Response<LoginMutation.Data> {
        return apolloClient.mutate(mutation).await()
    }

    suspend fun signup(mutation: SignupMutation): Response<SignupMutation.Data> = apolloClient.mutate(mutation).await()
    suspend fun updateDevice(mutation: UpdateDeviceMutation): Response<*> {
        return apolloClient.mutate(mutation).await()
    }

    companion object {
        const val BASE_URL = "http://smarthomeserver.us-west-2.elasticbeanstalk.com/graphql"
        const val BASE_URL_DEV = "http://58e8cb5a.ngrok.io/graphql"
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
        })
    }
}

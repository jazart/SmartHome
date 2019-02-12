package com.jazart.smarthome.repository

import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.graphql.UserQuery
import com.jazart.smarthome.network.SmartHomeService
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserRepo @Inject constructor(smartHomeService: SmartHomeService) {
    private val client = smartHomeService.apolloClient
    val user = MutableLiveData<String>()

    suspend fun <T> ApolloCall.Callback<T>.await() {

    }
    suspend fun awaitUserCall(): String? {
        return suspendCoroutine { cont ->
            client.query(UserQuery()).enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                override fun onResponse(response: Response<UserQuery.Data>) {
                    cont.resume(response.data()?.user()?.name())
                }

                override fun onFailure(e: ApolloException) {
                    cont.resumeWithException(e)
                }
            })
        }
    }
}
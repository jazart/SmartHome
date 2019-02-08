package com.jazart.smarthome.network

import com.apollographql.apollo.ApolloClient
import com.jazart.smarthome.models.Device
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST

interface SmartHomeApi{

    @POST("signin")
    fun signIn(name: String, pass: String){
        val client = ApolloClient.builder().build()
    }

    @GET("/users/{id}/devices")
    fun getDevices(id: String): Deferred<List<Device>>


}
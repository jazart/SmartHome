package com.jazart.smarthome.network

import android.content.Context
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import javax.inject.Inject

class SmartHomeService @Inject constructor(var apolloClient: ApolloClient) {

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
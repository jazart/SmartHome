package com.jazart.smarthome.network

import android.content.Context
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class SmartHomeService(
    private val context: Context,
    private val okhttp: OkHttpClient,
    var apolloClient: ApolloClient,
    private val tokenManager: TokenManager
) {

    init {
        okhttp.newBuilder()
            .addInterceptor(TokenInterceptor(tokenManager.token()))
            .authenticator(TokenAuthenticator(tokenManager))
        apolloClient = ApolloClient.builder().apply {
            serverUrl(BASE_URL)
            okHttpClient(okhttp)
        }.build()
    }

    companion object {
        const val BASE_URL = ""
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
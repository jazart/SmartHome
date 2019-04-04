package com.jazart.smarthome.network

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

typealias Token = String

class TokenInterceptor @Inject constructor(
    private val prefs: SharedPreferences
) : Interceptor {

    private val token: Token by lazy { prefs.getString("jwt", " ") }
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!token.isBlank()) {
            val req = chain.request().newBuilder()
               // .addHeader("Authorization", "Bearer ".plus(token))
                .build()

            return chain.proceed(req)
        }

        return chain.proceed(chain.request())
    }
}
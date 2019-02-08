package com.jazart.smarthome.network

import okhttp3.*

typealias Token = String

class TokenInterceptor(
    var token: Token,
    private var prevToken: Token = ""
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (token != prevToken) {
            prevToken = token
            val req = chain.request()
            req.newBuilder().apply {
                addHeader("Authorization", "bearer: $token")
                build()
            }
            return chain.proceed(req)
        }

        return chain.proceed(chain.request())
    }
}

class TokenAuthenticator(val tokenManager: TokenManager) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        response.header("Authorization: ")?.let { authHeader ->
            if (authHeader.startsWith("bearer:")) {
                // request new token then retry request.
                tokenManager.newToken()
            }
        }
        return null
    }
}
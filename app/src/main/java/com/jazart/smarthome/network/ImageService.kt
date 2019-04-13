package com.jazart.smarthome.network

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class ImageService @Inject constructor(private val okhttp: OkHttpClient) {

    suspend fun getImage(user: String): String? {
        val httpUrl = HttpUrl.Builder().run {
            scheme("http")
            host("6fa5ee6c.ngrok.io")
            addPathSegment("images")
            build()
        }
        val response = okhttp.newCall(Request.Builder().run {
            url(httpUrl)
            addHeader("Accept", "text/plain")
            method("GET", null)
            build()
        }).execute()
        return when {
            !response.isSuccessful || response.body().toString().isBlank() -> null
            else -> response.body()?.string()
        }
    }
}
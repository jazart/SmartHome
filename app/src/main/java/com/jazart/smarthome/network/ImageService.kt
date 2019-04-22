package com.jazart.smarthome.network

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class ImageService @Inject constructor(private val okhttp: OkHttpClient) {

    fun getImage(user: String): String? {
        val httpUrl = HttpUrl.Builder().run {
            scheme("http")
            host("42bfa5b9.ngrok.io")
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
            !response.isSuccessful -> null
            else -> response.body()?.string()
        }
    }
}
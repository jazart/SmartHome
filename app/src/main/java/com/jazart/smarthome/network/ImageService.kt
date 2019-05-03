package com.jazart.smarthome.network

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class ImageService @Inject constructor(private val okhttp: OkHttpClient) {

    fun getImage(user: String): String? {
        val httpUrl = HttpUrl.Builder().run {
            scheme("http")
//            host("6bf29ae7.ngrok.io")
            host("smarthomeserver.us-west-2.elasticbeanstalk.com")
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
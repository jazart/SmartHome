package com.jazart.smarthome.di

import com.apollographql.apollo.ApolloClient
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.network.TokenInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
object NetworkModule {

    @Provides
    fun provideOkhttp(): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(TokenInterceptor(""))
            .build()


    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder().apply {
            serverUrl(SmartHomeService.BASE_URL)
            okHttpClient(okHttpClient)
        }.build()
    }
}

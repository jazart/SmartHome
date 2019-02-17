package com.jazart.smarthome.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
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
        return ApolloClient.builder().run {
            serverUrl(SmartHomeService.BASE_URL)
            defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST)
            okHttpClient(okHttpClient)
            build()
        }
    }
}

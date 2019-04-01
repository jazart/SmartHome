package com.jazart.smarthome.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.network.TokenInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
object NetworkModule {

    @Provides
    fun provideOkhttp(tokenInterceptor: TokenInterceptor): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(tokenInterceptor)
            .build()


    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient, app: App): ApolloClient {
        return ApolloClient.builder().run {
            serverUrl(SmartHomeService.BASE_URL_DEV)
            httpCache(
                ApolloHttpCache(
                    DiskLruHttpCacheStore(app.applicationContext.cacheDir, Math.pow(1024.0, 4.0).toLong())
                )
            )
            defaultHttpCachePolicy(HttpCachePolicy.NETWORK_FIRST)
            okHttpClient(okHttpClient)
            build()
        }
    }
}

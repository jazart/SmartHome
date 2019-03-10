package com.jazart.smarthome.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.jazart.smarthome.network.SmartHomeService
import com.jazart.smarthome.network.TokenInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    @Provides
    fun provideOkhttp(tokenInterceptor: TokenInterceptor): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(tokenInterceptor)
            .build()


    @Provides
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder().run {
            serverUrl(SmartHomeService.BASE_URL_DEV)
            normalizedCache(
                LruNormalizedCacheFactory(
                    EvictionPolicy.builder()
                        .expireAfterAccess(30, TimeUnit.SECONDS)
                        .maxSizeBytes(10.times(1024).toLong())
                        .build()
                )
            )
            okHttpClient(okHttpClient)
            build()
        }
    }
}

package com.jazart.smarthome.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import dagger.*
import dagger.android.AndroidInjectionModule
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, AndroidInjectionModule::class, MainActivityModule::class, SharedPrefsModule::class])
interface AppComponent {
    val okHttpClient: OkHttpClient
    val apolloClient: ApolloClient

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun network(networkModule: NetworkModule): Builder
        fun sharedPreferences(sharedPreferences: SharedPrefsModule): Builder
        fun build(): AppComponent
    }
    fun inject(app: App)
}

@Module
object SharedPrefsModule {
    @Provides
    fun provideSharedPreferences(app: App): SharedPreferences = app.getSharedPreferences("user_jwt", MODE_PRIVATE)
}


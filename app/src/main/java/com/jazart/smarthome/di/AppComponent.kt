package com.jazart.smarthome.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.BinderThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apollographql.apollo.ApolloClient
import com.jazart.smarthome.LoginFragment
import com.jazart.smarthome.LoginViewModel
import com.jazart.smarthome.MainActivity
import dagger.*
import dagger.android.AndroidInjectionModule
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import javax.inject.Singleton
import kotlin.reflect.KClass

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
    fun provideSharedPreferences(app: App): SharedPreferences = app.getSharedPreferences("oauth", MODE_PRIVATE)
}


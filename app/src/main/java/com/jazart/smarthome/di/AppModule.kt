package com.jazart.smarthome.di

import android.app.Activity
import android.app.Application
import android.content.Context
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
@Component(modules = [(NetworkModule::class), (ViewModelModule::class), (AndroidInjectionModule::class), (MainActivityModule::class)])
interface AppComponent {
    val okHttpClient: OkHttpClient
    val apolloClient: ApolloClient

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun network(networkModule: NetworkModule): Builder
        fun build(): AppComponent
    }
    fun inject(app: App)
}



@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
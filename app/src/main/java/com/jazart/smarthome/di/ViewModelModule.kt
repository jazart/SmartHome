package com.jazart.smarthome.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jazart.smarthome.devicemgmt.DeviceViewModel
import com.jazart.smarthome.devicemgmt.HomeViewModel
import com.jazart.smarthome.login.LoginViewModel
import com.jazart.smarthome.login.SignupViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

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
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeviceViewModel::class)
    abstract fun bindDeviceViewModel(deviceViewModel: DeviceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun bindSignupViewModel(signupViewModel: SignupViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
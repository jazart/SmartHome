package com.jazart.smarthome.di

import com.jazart.smarthome.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDeviceFragment(): DeviceFragment

    @ContributesAndroidInjector
    abstract fun contributeDeviceCommandBottomSheet(): DeviceCommandBottomSheet
}
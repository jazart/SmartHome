package com.jazart.smarthome.di

import com.jazart.smarthome.common.MainActivity
import com.jazart.smarthome.common.SettingsFragment
import com.jazart.smarthome.devicemgmt.DeviceCommandBottomSheet
import com.jazart.smarthome.devicemgmt.DeviceFragment
import com.jazart.smarthome.devicemgmt.HomeFragment
import com.jazart.smarthome.login.LoginFragment
import com.jazart.smarthome.login.SignupFragment
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
    abstract fun contributeSignupFragment(): SignupFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDeviceFragment(): DeviceFragment

    @ContributesAndroidInjector
    abstract fun contributeDeviceCommandBottomSheet(): DeviceCommandBottomSheet
}
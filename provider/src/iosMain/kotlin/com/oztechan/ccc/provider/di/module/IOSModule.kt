package com.oztechan.ccc.provider.di.module

import com.oztechan.ccc.client.di.NativeDependencyWrapper
import com.oztechan.ccc.client.model.Device
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal fun getIOSModule(userDefaults: NSUserDefaults) = module {
    single { NativeDependencyWrapper(userDefaults) }
    single { Device.IOS }
}

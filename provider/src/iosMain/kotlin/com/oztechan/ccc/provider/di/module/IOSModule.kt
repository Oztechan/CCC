package com.oztechan.ccc.provider.di.module

import com.oztechan.ccc.common.di.NativeDependencyWrapper
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal fun getIOSModule(userDefaults: NSUserDefaults) = module {
    single { NativeDependencyWrapper(userDefaults) }
}

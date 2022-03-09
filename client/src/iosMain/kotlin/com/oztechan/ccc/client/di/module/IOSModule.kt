package com.oztechan.ccc.client.di.module

import com.github.mustafaozhan.ccc.common.di.NativeDependencyWrapper
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun getIOSModule(userDefaults: NSUserDefaults) = module {
    single { NativeDependencyWrapper(userDefaults) }
}

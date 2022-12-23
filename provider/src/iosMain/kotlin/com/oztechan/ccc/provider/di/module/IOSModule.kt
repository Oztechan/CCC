package com.oztechan.ccc.provider.di.module

import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal fun getIOSModule(userDefaults: NSUserDefaults) = module {
    single { userDefaults }
}

package com.github.mustafaozhan.ccc.client.di.module

import com.github.mustafaozhan.ccc.common.di.nsUserDefaults
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun getIOSModule(userDefaults: NSUserDefaults) = module {
    // https://github.com/InsertKoinIO/koin/issues/1016
    // todo koin doesn't support to have it as single then use with get() for Objective-C classes
    // single { userDefaults }
    nsUserDefaults = userDefaults
}

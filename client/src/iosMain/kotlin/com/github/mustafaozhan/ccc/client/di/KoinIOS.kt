/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.nsUserDefaults
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initIOS(userDefaults: NSUserDefaults) = initClient(
    module {
        // todo koin doesn't support to have it as single then use with get() for Objective-C classes
        nsUserDefaults = userDefaults
    }
).also {
    kermit.d { "KoinIOS initIOS" }
}

fun <T> Koin.getForIOS(objCClass: ObjCClass): T? = getOriginalKotlinClass(objCClass)?.let {
    getDependency(it)
}

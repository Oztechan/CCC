/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.nsUserDefaults
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
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

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    override: Boolean,
    noinline definition: Definition<T>
): BeanDefinition<T> = single(qualifier = qualifier, override = override, definition = definition)

fun <T> Koin.getDependency(objCClass: ObjCClass): T? = getOriginalKotlinClass(objCClass)?.let {
    getDependency(it)
}

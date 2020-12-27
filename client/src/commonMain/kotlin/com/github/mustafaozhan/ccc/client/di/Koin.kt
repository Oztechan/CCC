/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.di.initCommon
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun initClient(appModule: Module, useFakes: Boolean = false): KoinApplication = initCommon(
    appModule.plus(clientModule), useFakes
).also {
    kermit.d { "Koin initClient" }
}

expect val clientModule: Module

fun <T> Koin.getDependency(clazz: KClass<*>) = getDependency<T>(clazz)

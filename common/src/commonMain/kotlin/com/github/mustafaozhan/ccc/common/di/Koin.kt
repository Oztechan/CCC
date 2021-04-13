/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.logmob.initLogger
import com.github.mustafaozhan.logmob.kermit
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

const val DATABASE_NAME = "application_database.sqlite"

fun initCommon(
    clientModule: List<Module> = emptyList(),
    forTest: Boolean = false
) = startKoin {
    modules(clientModule)
    modules(getCommonModule(forTest))
}.also {
    initLogger(forTest)
    kermit.d { "Koin initCommon" }
}

@Suppress("UNCHECKED_CAST")
fun <T> Koin.getDependency(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}

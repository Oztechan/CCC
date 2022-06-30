/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.di

import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

// Names
const val DISPATCHER_IO = "DISPATCHER_IO"
const val DISPATCHER_MAIN = "DISPATCHER_MAIN"
const val DISPATCHER_UNCONFINED = "DISPATCHER_UNCONFINED"
const val DISPATCHER_DEFAULT = "DISPATCHER_DEFAULT"

@Suppress("UNCHECKED_CAST")
fun <T> Koin.getDependency(clazz: KClass<*>, qualifier: Qualifier? = null): T {
    return get(clazz, qualifier) { parametersOf(clazz.simpleName) } as T
}

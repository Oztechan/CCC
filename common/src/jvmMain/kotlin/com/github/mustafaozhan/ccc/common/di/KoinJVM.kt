/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.di

import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

fun <T> Koin.getForJvm(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}

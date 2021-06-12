/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.di.initCommon
import com.github.mustafaozhan.logmob.kermit
import org.koin.core.Koin
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

fun initClient(
    appModule: Module,
    forTest: Boolean = false
) = initCommon(
    appModule.plus(getClientModule()),
    forTest
).also {
    kermit.d { "Koin initClient" }
}

expect inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier? = null,
    createdAtStart: Boolean = false,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>>

fun <T> Koin.getDependency(clazz: KClass<*>) = getDependency<T>(clazz)

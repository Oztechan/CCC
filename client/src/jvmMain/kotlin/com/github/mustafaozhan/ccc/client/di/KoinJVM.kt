/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import java.util.prefs.Preferences
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

fun initJVM(delegate: Preferences) = initClient(
    module { single { delegate } }
).also {
    kermit.d { "KoinJVM initJVM" }
}

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    override: Boolean,
    noinline definition: Definition<T>
): BeanDefinition<T> = single(qualifier = qualifier, override = override, definition = definition)

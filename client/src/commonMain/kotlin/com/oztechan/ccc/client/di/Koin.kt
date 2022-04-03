/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.di

import com.oztechan.ccc.client.base.BaseViewModel
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

expect inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier? = null,
    createdAtStart: Boolean = false,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>>

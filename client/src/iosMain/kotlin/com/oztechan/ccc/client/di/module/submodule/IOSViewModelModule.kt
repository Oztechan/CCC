package com.oztechan.ccc.client.di.module.submodule

import com.oztechan.ccc.client.base.BaseViewModel
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    createdAtStart: Boolean,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>> = single(
    qualifier = qualifier,
    createdAtStart = createdAtStart,
    definition = definition
)

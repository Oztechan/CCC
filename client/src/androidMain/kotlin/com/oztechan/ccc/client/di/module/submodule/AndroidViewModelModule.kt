package com.oztechan.ccc.client.di.module.submodule

import com.oztechan.ccc.client.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): KoinDefinition<T> = viewModel(
    qualifier = qualifier,
    definition = definition
)

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.di

import android.content.Context
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.di.module.androidModule
import com.oztechan.ccc.client.di.module.clientModule
import com.oztechan.ccc.common.di.modules.apiModule
import com.oztechan.ccc.common.di.modules.dataSourceModule
import com.oztechan.ccc.common.di.modules.databaseModule
import com.oztechan.ccc.common.di.modules.dispatcherModule
import com.oztechan.ccc.common.di.modules.serviceModule
import com.oztechan.ccc.common.di.modules.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

fun initAndroid(
    context: Context,
    platformModule: Module
): KoinApplication = startKoin {
    androidContext(context)
    modules(
        androidModule,
        platformModule,
        clientModule,
        apiModule,
        serviceModule,
        databaseModule,
        settingsModule,
        dataSourceModule,
        dispatcherModule
    )
}.also {
    Logger.d { "KoinAndroid initAndroid" }
}

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    createdAtStart: Boolean,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>> = viewModel(
    qualifier = qualifier,
    definition = definition
)

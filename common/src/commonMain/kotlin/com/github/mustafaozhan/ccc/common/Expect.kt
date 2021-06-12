/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.russhwolf.settings.Settings
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

expect val platform: PlatformType

expect val platformCoroutineContext: CoroutineContext

expect fun Module.getSettingsDefinition(): Pair<Module, InstanceFactory<Settings>>

expect fun Module.getDatabaseDefinition(): Pair<Module, InstanceFactory<CurrencyConverterCalculatorDatabase>>

expect fun runTest(block: suspend () -> Unit)

expect fun isDebug(): Boolean

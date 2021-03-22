/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.github.mustafaozhan.ccc.common.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.Settings
import org.koin.core.definition.BeanDefinition
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

expect val platform: PlatformType

expect val platformCoroutineContext: CoroutineContext

expect fun Module.getSettingsDefinition(): BeanDefinition<Settings>

expect fun Module.getDatabaseDefinition(): BeanDefinition<CurrencyConverterCalculatorDatabase>

expect fun runTest(block: suspend () -> Unit)

expect fun isDebug(): Boolean

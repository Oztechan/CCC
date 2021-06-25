/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.di

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyConverterCalculatorDatabase
import com.russhwolf.settings.Settings
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module

expect fun Module.getSettingsDefinition(): Pair<Module, InstanceFactory<Settings>>

expect fun Module.getDatabaseDefinition(
    databaseName: String
): Pair<Module, InstanceFactory<CurrencyConverterCalculatorDatabase>>

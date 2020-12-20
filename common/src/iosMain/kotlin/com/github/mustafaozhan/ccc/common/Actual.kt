/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.di.DATABASE_NAME
import com.github.mustafaozhan.ccc.common.model.CommonPlatformType
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val commonPlatformType = CommonPlatformType.IOS

actual val platformCoroutineContext: CoroutineContext = Dispatchers.Default

actual val platformCommonModule: Module = module {
    single {
        CurrencyConverterCalculatorDatabase(
            NativeSqliteDriver(
                CurrencyConverterCalculatorDatabase.Schema,
                DATABASE_NAME
            )
        )
    }
}

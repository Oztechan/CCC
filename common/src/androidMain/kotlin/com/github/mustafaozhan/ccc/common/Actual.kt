/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.di.DATABASE_NAME
import com.github.mustafaozhan.ccc.common.model.CommonPlatformType
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val commonPlatformType = CommonPlatformType.ANDROID

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual val platformCommonModule: Module = module {
    single {
        CurrencyConverterCalculatorDatabase(
            AndroidSqliteDriver(
                CurrencyConverterCalculatorDatabase.Schema,
                get(),
                DATABASE_NAME
            )
        )
    }
}

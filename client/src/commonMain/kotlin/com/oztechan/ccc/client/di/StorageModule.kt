package com.oztechan.ccc.client.di

import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.app.AppStorageImpl
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val storageModule = module {
    singleOf(::AppStorageImpl) { bind<AppStorage>() }
    singleOf(::CalculatorStorageImpl) { bind<CalculatorStorage>() }
}

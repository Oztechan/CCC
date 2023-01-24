package com.oztechan.ccc.client.storage.calculator.di

import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientStorageCalculatorModule = module {
    singleOf(::CalculatorStorageImpl) { bind<CalculatorStorage>() }
}

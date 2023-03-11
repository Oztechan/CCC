package com.oztechan.ccc.client.storage.calculation.di

import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientStorageCalculationModule = module {
    singleOf(::CalculationStorageImpl) { bind<CalculationStorage>() }
}

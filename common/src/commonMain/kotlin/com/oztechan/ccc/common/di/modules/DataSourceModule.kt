package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSourceImpl
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.datasource.settings.SettingsDataSourceImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::SettingsDataSourceImp) { bind<SettingsDataSource>() }

    singleOf(::CurrencyDataSourceImpl) { bind<CurrencyDataSource>() }
    singleOf(::OfflineRatesDataSourceImpl) { bind<OfflineRatesDataSource>() }
}

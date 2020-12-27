/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val SECOND: Long = 1000
private const val DAY = (12 * 60 * 60 * SECOND)

private val apiRepository: ApiRepository by lazy { app.koin.getDependency(ApiRepository::class) }
private val offlineRatesDao: OfflineRatesDao by lazy { app.koin.getDependency(OfflineRatesDao::class) }

fun refreshApi() = GlobalScope.launch {
    kermit.d { "Api refreshApi" }
    while (isActive) {
        kermit.d { "refreshing" }
        updateCurrencies()

        delay(DAY)
    }
}

suspend fun updateCurrencies() {
    CurrencyType.values().forEach { base ->

        delay(SECOND)

        apiRepository
            .getRatesByBaseViaApi(base.name)
            .execute({ currencyResponse ->
                offlineRatesDao.insertOfflineRates(currencyResponse.rates)
            }, { error ->
                kermit.e(error) { error.message.toString() }
            })
    }
}

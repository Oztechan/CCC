/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ApiController(
    private val apiRepository: ApiRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) {
    companion object {
        private const val SECOND: Long = 1000
        private const val DAY = (12 * 60 * 60 * SECOND)
    }

    fun startSyncApi() = GlobalScope.launch {
        kermit.d { "Api refreshApi" }
        while (isActive) {
            kermit.d { "refreshing" }
            updateCurrencies()

            delay(DAY)
        }
    }

    private suspend fun updateCurrencies() {
        CurrencyType.values().forEach { base ->

            delay(SECOND)

            apiRepository
                .getRatesViaApi(base.name)
                .execute({ currencyResponse ->
                    offlineRatesRepository.insertOfflineRates(currencyResponse.rates)
                }, { error ->
                    kermit.e(error) { error.message.toString() }
                })
        }
    }
}

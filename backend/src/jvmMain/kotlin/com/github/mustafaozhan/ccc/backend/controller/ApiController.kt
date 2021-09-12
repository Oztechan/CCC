/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.controller

import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.SECOND
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ApiController(
    private val apiRepository: ApiRepository,
    private val offlineRatesRepository: OfflineRatesRepository
) {
    fun startSyncApi() = CoroutineScope(Dispatchers.IO).launch {
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
                    offlineRatesRepository.insertOfflineRates(currencyResponse)
                }, { error ->
                    kermit.e(error) { error.message.toString() }
                })
        }
    }
}

// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.data.room.offlineRates

import mustafaozhan.github.com.mycurrencies.model.Rates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineRatesRepository
@Inject constructor(
    private val offlineRatesDao: OfflineRatesDao
) {
    fun insertOfflineRates(rates: Rates) = offlineRatesDao.insertOfflineRates(rates)

    fun getOfflineRatesByBase(base: String) = offlineRatesDao.getOfflineRatesByBase(base)
}

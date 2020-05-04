/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.room.currency

import android.content.Context
import mustafaozhan.github.com.mycurrencies.extension.insertInitialCurrencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository
@Inject constructor(
    private val currencyDao: CurrencyDao,
    private val context: Context
) {

    fun getActiveCurrencies() = currencyDao.getActiveCurrencies()

    fun getAllCurrencies() = currencyDao.getAllCurrencies()

    suspend fun getCurrencyByName(name: String) = currencyDao.getCurrencyByName(name)

    suspend fun insertInitialCurrencies() = currencyDao.insertInitialCurrencies(context)

    suspend fun updateCurrencyStateByName(name: String, state: Boolean) =
        currencyDao.updateCurrencyStateByName(name, state)

    suspend fun updateAllCurrencyState(state: Boolean) = currencyDao.updateAllCurrencyState(state)
}

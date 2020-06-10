/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.db

import android.content.Context
import mustafaozhan.github.com.mycurrencies.di.ApplicationContext
import mustafaozhan.github.com.mycurrencies.util.extension.insertInitialCurrencies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBInitialise
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val currencyDao: CurrencyDao
) {
    suspend fun insertInitialCurrencies() = currencyDao.insertInitialCurrencies(context)
}

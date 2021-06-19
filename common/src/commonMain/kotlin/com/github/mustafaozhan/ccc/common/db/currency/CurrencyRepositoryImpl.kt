package com.github.mustafaozhan.ccc.common.db.currency

import com.github.mustafaozhan.ccc.common.db.sql.CurrencyQueries
import com.github.mustafaozhan.ccc.common.mapper.mapToModel
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toModelList
import com.github.mustafaozhan.logmob.kermit
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map

internal class CurrencyRepositoryImpl(
    private val currencyQueries: CurrencyQueries
) : CurrencyRepository {

    override fun collectAllCurrencies() = currencyQueries
        .collectAllCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .mapToModel()
        .also { kermit.d { "CurrencyRepositoryImpl collectAllCurrencies" } }

    override fun collectActiveCurrencies() = currencyQueries
        .collectActiveCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .mapToModel()
        .also { kermit.d { "CurrencyRepositoryImpl collectActiveCurrencies" } }

    override fun getActiveCurrencies() = currencyQueries
        .getActiveCurrencies()
        .executeAsList()
        .toModelList()
        .also { kermit.d { "CurrencyRepositoryImpl getActiveCurrencies" } }

    override fun updateCurrencyStateByName(name: String, isActive: Boolean) = currencyQueries
        .updateCurrencyStateByName(if (isActive) 1 else 0, name)
        .also { kermit.d { "CurrencyRepositoryImpl updateCurrencyStateByName $name $isActive" } }

    override fun updateAllCurrencyState(value: Boolean) = currencyQueries
        .updateAllCurrencyState(if (value) 1 else 0)
        .also { kermit.d { "CurrencyRepositoryImpl updateAllCurrencyState $value" } }

    override fun getCurrencyByName(name: String) = currencyQueries
        .getCurrencyByName(name)
        .executeAsOneOrNull()
        ?.toModel()
        .also { kermit.d { "CurrencyRepositoryImpl getCurrencyByName $name" } }
}

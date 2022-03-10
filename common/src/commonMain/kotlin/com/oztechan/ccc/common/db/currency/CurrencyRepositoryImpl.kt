package com.oztechan.ccc.common.db.currency

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.mapper.mapToModel
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toModelList
import com.oztechan.ccc.common.util.toDatabaseBoolean
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
        .also { Logger.v { "CurrencyRepositoryImpl collectAllCurrencies" } }

    override fun collectActiveCurrencies() = currencyQueries
        .collectActiveCurrencies()
        .asFlow()
        .mapToList()
        .map { it.sortedBy { (name) -> name } }
        .mapToModel()
        .also { Logger.v { "CurrencyRepositoryImpl collectActiveCurrencies" } }

    override fun getActiveCurrencies() = currencyQueries
        .getActiveCurrencies()
        .executeAsList()
        .toModelList()
        .also { Logger.v { "CurrencyRepositoryImpl getActiveCurrencies" } }

    override fun updateCurrencyStateByName(name: String, isActive: Boolean) = currencyQueries
        .updateCurrencyStateByName(isActive.toDatabaseBoolean(), name)
        .also { Logger.v { "CurrencyRepositoryImpl updateCurrencyStateByName $name $isActive" } }

    override fun updateAllCurrencyState(value: Boolean) = currencyQueries
        .updateAllCurrencyState(value.toDatabaseBoolean())
        .also { Logger.v { "CurrencyRepositoryImpl updateAllCurrencyState $value" } }

    override fun getCurrencyByName(name: String) = currencyQueries
        .getCurrencyByName(name)
        .executeAsOneOrNull()
        ?.toModel()
        .also { Logger.v { "CurrencyRepositoryImpl getCurrencyByName $name" } }
}

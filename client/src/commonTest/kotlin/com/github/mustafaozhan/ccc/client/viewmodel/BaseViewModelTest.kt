/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.common.Currency
import com.github.mustafaozhan.ccc.common.CurrencyQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn

abstract class BaseViewModelTest<ViewModelType> : CurrencyQueries {
    protected abstract var viewModel: ViewModelType

    override fun <T : Any> collectAllCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun collectAllCurrencies(): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> collectActiveCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun collectActiveCurrencies(): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getActiveCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun getActiveCurrencies(): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getCurrencyByName(
        name: String,
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun getCurrencyByName(name: String): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun updateCurrencyStateByName(isActive: Long, name: String) {
        TODO("Not yet implemented")
    }

    override fun updateAllCurrencyState(isActive: Long) {
        TODO("Not yet implemented")
    }

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(
        noEnclosing: Boolean,
        bodyWithReturn: TransactionWithReturn<R>.() -> R
    ): R {
        TODO("Not yet implemented")
    }
}

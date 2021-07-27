/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.fake

import com.github.mustafaozhan.ccc.common.db.sql.Currency
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn

@Suppress("TooManyFunctions", "StringLiteralDuplication", "UNCHECKED_CAST")
object FakeCurrencyQueries : CurrencyQueries {

    private val FAKE_CURRENCY = Currency("EUR", "Euro", "sad", 0.0, 0.toLong())

    fun getCurrencyQueries(): CurrencyQueries = this

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) = Unit

    override fun <R> transactionWithResult(
        noEnclosing: Boolean,
        bodyWithReturn: TransactionWithReturn<R>.() -> R
    ): R {
        TODO("Fake method Not yet implemented")
    }

    override fun <T : Any> collectAllCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ) = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    } as Query<T>

    override fun collectAllCurrencies(): Query<Currency> = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    }

    override fun <T : Any> collectActiveCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ) = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    } as Query<T>

    override fun collectActiveCurrencies() = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    }

    override fun <T : Any> getActiveCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ) = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    } as Query<T>

    override fun getActiveCurrencies(): Query<Currency> = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    }

    override fun <T : Any> getCurrencyByName(
        name: String,
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ) = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    } as Query<T>

    override fun getCurrencyByName(name: String) = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_CURRENCY
    }

    override fun updateCurrencyStateByName(isActive: Long, name: String) = Unit

    override fun updateAllCurrencyState(isActive: Long) = Unit
}

package com.oztechan.ccc.client.datasource.currency

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.mapper.toLong
import com.oztechan.ccc.common.core.database.sql.Currency
import com.oztechan.ccc.common.core.database.sql.CurrencyQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CurrencyDataSourceTest {

    private val subject: CurrencyDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        CurrencyDataSourceImpl(currencyQueries, UnconfinedTestDispatcher())
    }

    private val currencyQueries = mock<CurrencyQueries>(MockMode.autoUnit)
    private val sqlDriver = mock<SqlDriver>()
    private val sqlCursor = mock<SqlCursor>(MockMode.autoUnit)

    private val currency = Currency("EUR", "", "", 0.0, 0L)
    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        currency
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        every { sqlDriver.executeQuery(-1, "", 0, null) }
            .returns(sqlCursor)

        every { sqlCursor.next() }
            .returns(false)
    }

    @Test
    fun getCurrenciesFlow() {
        every { currencyQueries.getCurrencies() }
            .returns(query)

        runTest {
            subject.getCurrenciesFlow()
        }

        verify { currencyQueries.getCurrencies() }
    }

    @Test
    fun getActiveCurrenciesFlow() {
        every { currencyQueries.getActiveCurrencies() }
            .returns(query)

        runTest {
            subject.getActiveCurrenciesFlow()
        }

        verify { currencyQueries.getActiveCurrencies() }
    }

    @Test
    fun getActiveCurrencies() {
        every { currencyQueries.getActiveCurrencies() }
            .returns(query)

        runTest {
            subject.getActiveCurrencies()
        }

        verify { currencyQueries.getActiveCurrencies() }
    }

    @Test
    fun updateCurrencyStateByCode() {
        val mockCode = "mock"
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateCurrencyStateByCode(mockCode, mockState)
        }

        verify { currencyQueries.updateCurrencyStateByCode(mockState.toLong(), mockCode) }
    }

    @Test
    fun updateCurrencyStates() {
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateCurrencyStates(mockState)
        }

        verify { currencyQueries.updateCurrencyStates(mockState.toLong()) }
    }

    @Test
    fun getCurrencyByCode() {
        every { currencyQueries.getCurrencyByCode(currency.code) }
            .returns(query)

        runTest {
            subject.getCurrencyByCode(currency.code)
        }

        verify { currencyQueries.getCurrencyByCode(currency.code) }
    }
}

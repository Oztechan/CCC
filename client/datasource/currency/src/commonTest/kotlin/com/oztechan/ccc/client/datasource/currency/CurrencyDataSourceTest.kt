package com.oztechan.ccc.client.datasource.currency

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.mapper.toLong
import com.oztechan.ccc.common.core.database.sql.Currency
import com.oztechan.ccc.common.core.database.sql.CurrencyQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class CurrencyDataSourceTest {

    private val subject: CurrencyDataSource by lazy {
        CurrencyDataSourceImpl(currencyQueries, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @Mock
    private val currencyQueries = mock(classOf<CurrencyQueries>())

    @Mock
    private val sqlDriver = mock(classOf<SqlDriver>())

    @Mock
    private val sqlCursor = mock(classOf<SqlCursor>())

    private val currency = Currency("EUR", "", "", 0.0, 0L)
    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        currency
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        given(sqlDriver)
            .invocation { executeQuery(-1, "", 0, null) }
            .thenReturn(sqlCursor)

        given(sqlCursor)
            .invocation { next() }
            .thenReturn(false)
    }

    @Test
    fun getCurrenciesFlow() {
        given(currencyQueries)
            .invocation { getCurrencies() }
            .thenReturn(query)

        runTest {
            subject.getCurrenciesFlow()
        }

        verify(currencyQueries)
            .invocation { getCurrencies() }
            .wasInvoked()
    }

    @Test
    fun getActiveCurrenciesFlow() {
        given(currencyQueries)
            .invocation { getActiveCurrencies() }
            .thenReturn(query)

        runTest {
            subject.getActiveCurrenciesFlow()
        }

        verify(currencyQueries)
            .invocation { getActiveCurrencies() }
            .wasInvoked()
    }

    @Test
    fun getActiveCurrencies() {
        given(currencyQueries)
            .invocation { getActiveCurrencies() }
            .thenReturn(query)

        runTest {
            subject.getActiveCurrencies()
        }

        verify(currencyQueries)
            .invocation { getActiveCurrencies() }
            .wasInvoked()
    }

    @Test
    fun updateCurrencyStateByCode() {
        val mockCode = "mock"
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateCurrencyStateByCode(mockCode, mockState)
        }

        verify(currencyQueries)
            .invocation { updateCurrencyStateByCode(mockState.toLong(), mockCode) }
            .wasInvoked()
    }

    @Test
    fun updateCurrencyStates() {
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateCurrencyStates(mockState)
        }

        verify(currencyQueries)
            .invocation { updateCurrencyStates(mockState.toLong()) }
            .wasInvoked()
    }

    @Test
    fun getCurrencyByCode() {
        given(currencyQueries)
            .invocation { getCurrencyByCode(currency.code) }
            .thenReturn(query)

        runTest {
            subject.getCurrencyByCode(currency.code)
        }

        verify(currencyQueries)
            .invocation { getCurrencyByCode(currency.code) }
            .wasInvoked()
    }
}

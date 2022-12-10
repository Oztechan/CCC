package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.db.sql.Currency
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class CurrencyDataSourceTest : BaseSubjectTest<CurrencyDataSource>() {

    override val subject: CurrencyDataSource by lazy {
        CurrencyDataSourceImpl(currencyQueries, createTestDispatcher())
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
    override fun setup() {
        super.setup()

        given(sqlDriver)
            .invocation { executeQuery(-1, "", 0, null) }
            .thenReturn(sqlCursor)

        given(sqlCursor)
            .invocation { next() }
            .thenReturn(false)
    }

    @Test
    fun collectAllCurrencies() {
        given(currencyQueries)
            .invocation { collectAllCurrencies() }
            .thenReturn(query)

        runTest {
            subject.collectAllCurrencies()
        }

        verify(currencyQueries)
            .invocation { collectAllCurrencies() }
            .wasInvoked()
    }

    @Test
    fun collectActiveCurrencies() {
        given(currencyQueries)
            .invocation { collectActiveCurrencies() }
            .thenReturn(query)

        runTest {
            subject.collectActiveCurrencies()
        }

        verify(currencyQueries)
            .invocation { collectActiveCurrencies() }
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
    fun updateAllCurrencyState() {
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateAllCurrencyState(mockState)
        }

        verify(currencyQueries)
            .invocation { updateAllCurrencyState(mockState.toLong()) }
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

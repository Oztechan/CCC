package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
import com.oztechan.ccc.common.datasource.rates.RatesDataSource
import com.oztechan.ccc.common.datasource.rates.RatesDataSourceImpl
import com.oztechan.ccc.common.db.sql.RatesQueries
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toRates
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
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class RatesDataSourceTest : BaseSubjectTest<RatesDataSource>() {

    override val subject: RatesDataSource by lazy {
        RatesDataSourceImpl(ratesQueries, createTestDispatcher())
    }

    @Mock
    private val ratesQueries = mock(classOf<RatesQueries>())

    @Mock
    private val sqlDriver = mock(classOf<SqlDriver>())

    @Mock
    private val sqlCursor = mock(classOf<SqlCursor>())

    private val currencyResponseEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    private val currencyResponse = currencyResponseEntity.toModel()

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        currencyResponse.toRates()
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
    fun insertRates() {
        runTest {
            subject.insertRates(currencyResponse)
        }

        verify(ratesQueries)
            .invocation { insertRates(currencyResponse.toRates()) }
            .wasInvoked()
    }

    @Test
    fun getRatesByBase() {
        given(ratesQueries)
            .invocation { getRatesByBase(currencyResponse.base) }
            .then { query }

        runTest {
            subject.getRatesByBase(currencyResponse.base)
        }

        verify(ratesQueries)
            .invocation { getRatesByBase(currencyResponse.base) }
            .wasInvoked()
    }

    @Test
    fun getCurrencyResponseTextByBase() {
        given(ratesQueries)
            .invocation { getRatesByBase(currencyResponse.base) }
            .then { query }

        runTest {
            subject.getCurrencyResponseTextByBase(currencyResponse.base)
        }

        verify(ratesQueries)
            .invocation { getRatesByBase(currencyResponse.base) }
            .wasInvoked()
    }
}

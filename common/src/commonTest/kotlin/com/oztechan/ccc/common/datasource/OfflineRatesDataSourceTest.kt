package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSourceImpl
import com.oztechan.ccc.common.db.sql.OfflineRatesQueries
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toOfflineRates
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
class OfflineRatesDataSourceTest : BaseSubjectTest<OfflineRatesDataSource>() {

    override val subject: OfflineRatesDataSource by lazy {
        OfflineRatesDataSourceImpl(offlineRatesQueries, createTestDispatcher())
    }

    @Mock
    private val offlineRatesQueries = mock(classOf<OfflineRatesQueries>())

    @Mock
    private val sqlDriver = mock(classOf<SqlDriver>())

    @Mock
    private val sqlCursor = mock(classOf<SqlCursor>())

    private val currencyResponseEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    private val currencyResponse = currencyResponseEntity.toModel()

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        currencyResponse.toOfflineRates()
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
    fun insertOfflineRates() {
        runTest {
            subject.insertOfflineRates(currencyResponse)
        }

        verify(offlineRatesQueries)
            .invocation { insertOfflineRates(currencyResponse.toOfflineRates()) }
            .wasInvoked()
    }

    @Test
    fun getOfflineRatesByBase() {
        given(offlineRatesQueries)
            .invocation { getOfflineRatesByBase(currencyResponse.base) }
            .then { query }

        runTest {
            subject.getOfflineRatesByBase(currencyResponse.base)
        }

        verify(offlineRatesQueries)
            .invocation { getOfflineRatesByBase(currencyResponse.base) }
            .wasInvoked()
    }

    @Test
    fun getOfflineCurrencyResponseByBase() {
        given(offlineRatesQueries)
            .invocation { getOfflineRatesByBase(currencyResponse.base) }
            .then { query }

        runTest {
            subject.getOfflineCurrencyResponseByBase(currencyResponse.base)
        }

        verify(offlineRatesQueries)
            .invocation { getOfflineRatesByBase(currencyResponse.base) }
            .wasInvoked()
    }
}

package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSourceImpl
import com.oztechan.ccc.common.db.sql.OfflineRatesQueries
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toOfflineRates
import com.oztechan.ccc.test.BaseSubjectTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
class OfflineRatesDataSourceTest : BaseSubjectTest<OfflineRatesDataSource>() {

    override val subject: OfflineRatesDataSource by lazy {
        OfflineRatesDataSourceImpl(offlineRatesQueries, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @Mock
    private val offlineRatesQueries = mock(classOf<OfflineRatesQueries>())

    private val currencyResponseEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    private val currencyResponse = currencyResponseEntity.toModel()

    @Test
    fun insertOfflineRates() {
        runTest {
            subject.insertOfflineRates(currencyResponse)
        }

        verify(offlineRatesQueries)
            .invocation { insertOfflineRates(currencyResponse.toOfflineRates()) }
            .wasInvoked()
    }
}

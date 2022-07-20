package com.oztechan.ccc.common.datasource

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSourceImpl
import com.oztechan.ccc.common.db.sql.OfflineRatesQueries
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toOfflineRates
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

class OfflineRatesDataSourceTest {

    @Mock
    private val offlineRatesQueries = mock(classOf<OfflineRatesQueries>())

    private val dataSource: OfflineRatesDataSource by lazy {
        OfflineRatesDataSourceImpl(offlineRatesQueries)
    }

    private val currencyResponseEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    private val currencyResponse = currencyResponseEntity.toModel()

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun insertOfflineRates() {
        dataSource.insertOfflineRates(currencyResponse)

        verify(offlineRatesQueries)
            .invocation { insertOfflineRates(currencyResponse.toOfflineRates()) }
            .wasInvoked()
    }
}

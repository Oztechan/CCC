package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.entity.RatesEntity
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toOfflineRates
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.ConfigurationApi
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.eq
import io.mockative.mock
import io.mockative.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

@ConfigurationApi
class OfflineRatesRepositoryTest {

    @Mock
    private val offlineRatesQueries = configure(mock(classOf<OfflineRatesQueries>())) {
        stubsUnitByDefault = true
    }

    private val repository: OfflineRatesRepository by lazy {
        OfflineRatesRepositoryImpl(offlineRatesQueries)
    }

    private val currencyResponseEntity = CurrencyResponseEntity("EUR", "12.21.2121", RatesEntity())
    private val currencyResponse = currencyResponseEntity.toModel()

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun insertOfflineRates() {
        repository.insertOfflineRates(currencyResponse)

        verify(offlineRatesQueries)
            .function(offlineRatesQueries::insertOfflineRates)
            .with(eq(currencyResponse.toOfflineRates()))
            .wasInvoked()
    }
}

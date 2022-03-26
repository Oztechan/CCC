package com.oztechan.ccc.common.repo

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.oztechan.ccc.common.db.sql.OfflineRatesQueries
import com.oztechan.ccc.common.entity.CurrencyResponseEntity
import com.oztechan.ccc.common.entity.RatesEntity
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.mapper.toOfflineRates
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

class OfflineRatesRepositoryTest {

    @Mock
    private val offlineRatesQueries = mock(classOf<OfflineRatesQueries>())

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
            .invocation { insertOfflineRates(currencyResponse.toOfflineRates()) }
            .wasInvoked()
    }
}

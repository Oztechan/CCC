package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toOfflineRates
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.matching
import io.mockative.mock
import io.mockative.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class OfflineRatesRepositoryTest {

    @Mock
    private val offlineRatesQueries = mock(classOf<OfflineRatesQueries>())

    private val repository: OfflineRatesRepository by lazy {
        OfflineRatesRepositoryImpl(offlineRatesQueries)
    }

    val currencyResponse = CurrencyResponse("EUR", "12.21.2121", Rates())

    @Test
    fun insertOfflineRates() {
        given(offlineRatesQueries)
            .function(offlineRatesQueries::insertOfflineRates)
            .whenInvokedWith(matching { it == currencyResponse.toOfflineRates() })
            .thenReturn(Unit)

        repository.insertOfflineRates(currencyResponse)

        verify(offlineRatesQueries)
            .function(offlineRatesQueries::insertOfflineRates)
            .with(matching { it == currencyResponse.toOfflineRates() })
            .wasInvoked()
    }

    @Test
    fun getOfflineRatesByBase() {
        given(offlineRatesQueries)
            .invocation {
                offlineRatesQueries
                    .getOfflineRatesByBase(currencyResponse.base)
                    .executeAsOneOrNull()
                    ?.toModel()
            }
            .thenReturn(currencyResponse.rates)

        assertEquals(
            currencyResponse.rates,
            repository.getOfflineRatesByBase(currencyResponse.base)
        )
    }
}

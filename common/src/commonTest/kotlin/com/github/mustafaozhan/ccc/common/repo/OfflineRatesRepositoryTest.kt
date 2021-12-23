package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.entity.RatesEntity
import com.github.mustafaozhan.ccc.common.mapper.toCurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toOfflineRates
import com.github.mustafaozhan.ccc.common.mapper.toSerializedString
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

    private val currencyResponseEntity = CurrencyResponseEntity("EUR", "12.21.2121", RatesEntity())
    private val currencyResponse = currencyResponseEntity.toModel()

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

    @Test
    fun getOfflineCurrencyResponseByBase() {
        given(offlineRatesQueries)
            .invocation {
                offlineRatesQueries
                    .getOfflineRatesByBase(currencyResponse.base.uppercase())
                    .executeAsOneOrNull()
                    ?.toCurrencyResponseEntity()
                    ?.toSerializedString()
            }
            .thenReturn(currencyResponseEntity.toSerializedString())

        assertEquals(
            currencyResponseEntity.toSerializedString(),
            repository.getOfflineCurrencyResponseByBase(currencyResponse.base)
        )
    }
}

package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyQueries
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.ConfigurationApi
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.eq
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@ConfigurationApi
class CurrencyRepositoryTest {

    @Mock
    private val currencyQueries = configure(mock(classOf<CurrencyQueries>())) {
        stubsUnitByDefault = true
    }

    private val repository: CurrencyRepository by lazy {
        CurrencyRepositoryImpl(currencyQueries)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun updateCurrencyStateByName() {
        val mockName = "mock"
        val mockState = Random.nextBoolean()
        val databaseValue = if (mockState) 1.toLong() else 0.toLong()
        repository.updateCurrencyStateByName(mockName, mockState)

        verify(currencyQueries)
            .function(currencyQueries::updateCurrencyStateByName)
            .with(eq(mockName), eq(databaseValue))
            .wasInvoked()
    }

    @Test
    fun updateAllCurrencyState() {
        val mockState = Random.nextBoolean()
        val databaseValue = if (mockState) 1.toLong() else 0.toLong()

        repository.updateAllCurrencyState(mockState)

        verify(currencyQueries)
            .function(currencyQueries::updateAllCurrencyState)
            .with(eq(databaseValue))
            .wasInvoked()
    }
}

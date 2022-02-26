package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyQueries
import com.github.mustafaozhan.ccc.common.util.toDatabaseBoolean
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

class CurrencyRepositoryTest {

    @Mock
    private val currencyQueries = mock(classOf<CurrencyQueries>())

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

        repository.updateCurrencyStateByName(mockName, mockState)

        verify(currencyQueries)
            .invocation { updateCurrencyStateByName(mockState.toDatabaseBoolean(), mockName) }
            .wasInvoked()
    }

    @Test
    fun updateAllCurrencyState() {
        val mockState = Random.nextBoolean()

        repository.updateAllCurrencyState(mockState)

        verify(currencyQueries)
            .invocation { updateAllCurrencyState(mockState.toDatabaseBoolean()) }
            .wasInvoked()
    }
}

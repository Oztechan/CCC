package com.oztechan.ccc.common.datasource

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.mapper.toLong
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

    private val dataSource: CurrencyDataSource by lazy {
        CurrencyDataSourceImpl(currencyQueries)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun updateCurrencyStateByName() {
        val mockName = "mock"
        val mockState = Random.nextBoolean()

        dataSource.updateCurrencyStateByName(mockName, mockState)

        verify(currencyQueries)
            .invocation { updateCurrencyStateByName(mockState.toLong(), mockName) }
            .wasInvoked()
    }

    @Test
    fun updateAllCurrencyState() {
        val mockState = Random.nextBoolean()

        dataSource.updateAllCurrencyState(mockState)

        verify(currencyQueries)
            .invocation { updateAllCurrencyState(mockState.toLong()) }
            .wasInvoked()
    }
}

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
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
class CurrencyDataSourceTest {

    @Mock
    private val currencyQueries = mock(classOf<CurrencyQueries>())

    private val dataSource: CurrencyDataSource by lazy {
        CurrencyDataSourceImpl(currencyQueries, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun updateCurrencyStateByName() {
        val mockName = "mock"
        val mockState = Random.nextBoolean()

        runTest {
            dataSource.updateCurrencyStateByName(mockName, mockState)
        }

        verify(currencyQueries)
            .invocation { updateCurrencyStateByName(mockState.toLong(), mockName) }
            .wasInvoked()
    }

    @Test
    fun updateAllCurrencyState() {
        val mockState = Random.nextBoolean()

        runTest {
            dataSource.updateAllCurrencyState(mockState)
        }

        verify(currencyQueries)
            .invocation { updateAllCurrencyState(mockState.toLong()) }
            .wasInvoked()
    }
}

package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
class CurrencyDataSourceTest : BaseSubjectTest<CurrencyDataSource>() {

    override val subject: CurrencyDataSource by lazy {
        CurrencyDataSourceImpl(currencyQueries, createTestDispatcher())
    }

    @Mock
    private val currencyQueries = mock(classOf<CurrencyQueries>())

    @Test
    fun updateCurrencyStateByName() {
        val mockName = "mock"
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateCurrencyStateByName(mockName, mockState)
        }

        verify(currencyQueries)
            .invocation { updateCurrencyStateByName(mockState.toLong(), mockName) }
            .wasInvoked()
    }

    @Test
    fun updateAllCurrencyState() {
        val mockState = Random.nextBoolean()

        runTest {
            subject.updateAllCurrencyState(mockState)
        }

        verify(currencyQueries)
            .invocation { updateAllCurrencyState(mockState.toLong()) }
            .wasInvoked()
    }
}

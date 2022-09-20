package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSourceImpl
import com.oztechan.ccc.common.db.sql.CurrencyQueries
import com.oztechan.ccc.common.error.DatabaseException
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@Suppress("OPT_IN_USAGE")
class BaseDataSourceTest : BaseSubjectTest<CurrencyDataSource>() {
    override val subject: CurrencyDataSource by lazy {
        CurrencyDataSourceImpl(currencyQueries, createTestDispatcher())
    }

    @Mock
    private val currencyQueries = mock(classOf<CurrencyQueries>())

    @Test
    fun `any exception returns DatabaseException`() = runTest {
        val exception = Exception("Test exception")
        given(currencyQueries)
            .invocation { getActiveCurrencies() }
            .thenThrow(exception)

        assertFailsWith(DatabaseException::class) {
            subject.getActiveCurrencies()
        }.let {
            assertNotNull(it.cause)
            assertEquals(exception, it.cause!!.cause)
            assertNotNull(it.message)
            assertEquals(exception.message, it.message)
        }
    }
}

package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.api.model.Conversion
import com.oztechan.ccc.common.api.model.ExchangeRate
import com.oztechan.ccc.common.core.database.sql.ConversionQueries
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSourceImpl
import com.oztechan.ccc.common.mapper.toConversionDBModel
import com.oztechan.ccc.common.mapper.toExchangeRateModel
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class ConversionDataSourceTest : BaseSubjectTest<ConversionDataSource>() {

    override val subject: ConversionDataSource by lazy {
        ConversionDataSourceImpl(conversionQueries, createTestDispatcher())
    }

    @Mock
    private val conversionQueries = mock(classOf<ConversionQueries>())

    @Mock
    private val sqlDriver = mock(classOf<SqlDriver>())

    @Mock
    private val sqlCursor = mock(classOf<SqlCursor>())

    private val exchangeRateAPIModel = ExchangeRate("EUR", "12.21.2121", Conversion())
    private val exchangeRate = exchangeRateAPIModel.toExchangeRateModel()

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        exchangeRate.toConversionDBModel()
    }

    @BeforeTest
    override fun setup() {
        super.setup()

        given(sqlDriver)
            .invocation { executeQuery(-1, "", 0, null) }
            .thenReturn(sqlCursor)

        given(sqlCursor)
            .invocation { next() }
            .thenReturn(false)
    }

    @Test
    fun insertConversion() {
        runTest {
            subject.insertConversion(exchangeRate)
        }

        verify(conversionQueries)
            .invocation { insertConversion(exchangeRate.toConversionDBModel()) }
            .wasInvoked()
    }

    @Test
    fun getConversionByBase() {
        given(conversionQueries)
            .invocation { getConversionByBase(exchangeRate.base) }
            .then { query }

        runTest {
            subject.getConversionByBase(exchangeRate.base)
        }

        verify(conversionQueries)
            .invocation { getConversionByBase(exchangeRate.base) }
            .wasInvoked()
    }

    @Test
    fun getExchangeRateTextByBase() {
        given(conversionQueries)
            .invocation { getConversionByBase(exchangeRate.base) }
            .then { query }

        runTest {
            subject.getExchangeRateTextByBase(exchangeRate.base)
        }

        verify(conversionQueries)
            .invocation { getConversionByBase(exchangeRate.base) }
            .wasInvoked()
    }
}

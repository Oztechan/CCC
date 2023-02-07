package com.oztechan.ccc.common.datasource.conversion

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.ConversionQueries
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.ExchangeRate
import com.oztechan.ccc.common.datasource.conversion.mapper.toConversionDBModel
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@Suppress("OPT_IN_USAGE")
internal class ConversionDataSourceTest {

    private val subject: ConversionDataSource by lazy {
        ConversionDataSourceImpl(conversionQueries, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @Mock
    private val conversionQueries = configure(mock(classOf<ConversionQueries>())) { stubsUnitByDefault = true }

    @Mock
    private val sqlDriver = mock(classOf<SqlDriver>())

    @Mock
    private val sqlCursor = configure(mock(classOf<SqlCursor>())) { stubsUnitByDefault = true }

    private val exchangeRate = ExchangeRate("EUR", "12.21.2121", Conversion())

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        exchangeRate.toConversionDBModel()
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

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
}

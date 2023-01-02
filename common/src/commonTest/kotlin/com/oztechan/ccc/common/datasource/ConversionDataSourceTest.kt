package com.oztechan.ccc.common.datasource

import com.oztechan.ccc.common.api.model.Conversion
import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.database.sql.ConversionQueries
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSourceImpl
import com.oztechan.ccc.common.mapper.toConversion
import com.oztechan.ccc.common.mapper.toModel
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

    private val currencyResponseEntity = CurrencyResponse("EUR", "12.21.2121", Conversion())
    private val currencyResponse = currencyResponseEntity.toModel()

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        currencyResponse.toConversion()
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
            subject.insertConversion(currencyResponse)
        }

        verify(conversionQueries)
            .invocation { insertConversion(currencyResponse.toConversion()) }
            .wasInvoked()
    }

    @Test
    fun getConversionByBase() {
        given(conversionQueries)
            .invocation { getConversionByBase(currencyResponse.base) }
            .then { query }

        runTest {
            subject.getConversionByBase(currencyResponse.base)
        }

        verify(conversionQueries)
            .invocation { getConversionByBase(currencyResponse.base) }
            .wasInvoked()
    }

    @Test
    fun getCurrencyResponseTextByBase() {
        given(conversionQueries)
            .invocation { getConversionByBase(currencyResponse.base) }
            .then { query }

        runTest {
            subject.getCurrencyResponseTextByBase(currencyResponse.base)
        }

        verify(conversionQueries)
            .invocation { getConversionByBase(currencyResponse.base) }
            .wasInvoked()
    }
}

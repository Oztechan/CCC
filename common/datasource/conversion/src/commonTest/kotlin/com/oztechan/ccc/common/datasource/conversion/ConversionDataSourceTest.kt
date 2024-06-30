package com.oztechan.ccc.common.datasource.conversion

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.ConversionQueries
import com.oztechan.ccc.common.datasource.conversion.fakes.Fakes
import com.oztechan.ccc.common.datasource.conversion.mapper.toConversionDBModel
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class ConversionDataSourceTest {

    private val subject: ConversionDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        ConversionDataSourceImpl(conversionQueries, UnconfinedTestDispatcher())
    }

    private val conversionQueries = mock<ConversionQueries>(MockMode.autoUnit)
    private val sqlDriver = mock<SqlDriver>()
    private val sqlCursor = mock<SqlCursor>(MockMode.autoUnit)

    private val query = Query(-1, mutableListOf(), sqlDriver, query = "") {
        Fakes.conversionModel.toConversionDBModel()
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        every { sqlDriver.executeQuery(-1, "", 0, null) }
            .returns(sqlCursor)

        every { sqlCursor.next() }
            .returns(false)
    }

    @Test
    fun insertConversion() {
        runTest {
            subject.insertConversion(Fakes.conversionModel)
        }

        verify { conversionQueries.insertConversion(Fakes.conversionModel.toConversionDBModel()) }
    }

    @Test
    fun getConversionByBase() {
        every { conversionQueries.getConversionByBase(Fakes.conversionModel.base) }
            .returns(query)

        runTest {
            subject.getConversionByBase(Fakes.conversionModel.base)
        }

        verify { conversionQueries.getConversionByBase(Fakes.conversionModel.base) }
    }
}

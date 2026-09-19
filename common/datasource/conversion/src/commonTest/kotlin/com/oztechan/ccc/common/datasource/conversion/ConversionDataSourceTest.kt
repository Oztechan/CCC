package com.oztechan.ccc.common.datasource.conversion

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import com.oztechan.ccc.common.datasource.conversion.fakes.Fakes
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ConversionDataSourceTest {

    private val driver = createTestSqlDriver()

    private val subject: ConversionDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        ConversionDataSourceImpl(
            CurrencyConverterCalculatorDatabase(driver).conversionQueries,
            UnconfinedTestDispatcher()
        )
    }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun insertConversion() = runTest {
        subject.insertConversion(Fakes.conversionModel)

        assertEquals(
            Fakes.conversionModel,
            subject.getConversionByBase(Fakes.conversionModel.base)
        )
    }

    @Test
    fun insertConversionReplacesExistingBase() = runTest {
        subject.insertConversion(Fakes.conversionModel)
        subject.insertConversion(Fakes.conversionModel.copy(date = "01.01.2023"))

        assertEquals(
            "01.01.2023",
            subject.getConversionByBase(Fakes.conversionModel.base)?.date
        )
    }

    @Test
    fun getConversionByBase() = runTest {
        assertNull(subject.getConversionByBase(Fakes.conversionModel.base))

        subject.insertConversion(Fakes.conversionModel)

        assertEquals(
            Fakes.conversionModel,
            subject.getConversionByBase(Fakes.conversionModel.base)
        )
    }

    @Test
    fun getConversionByBaseReturnsNullForUnknownBase() = runTest {
        subject.insertConversion(Fakes.conversionModel)

        assertNull(subject.getConversionByBase("UNKNOWN"))
    }
}

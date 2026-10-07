package com.oztechan.ccc.client.datasource.currency

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.database.sql.CurrencyConverterCalculatorDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CurrencyDataSourceTest {

    private val driver = createTestSqlDriver()

    private val subject: CurrencyDataSource by lazy {
        @Suppress("OPT_IN_USAGE")
        CurrencyDataSourceImpl(
            CurrencyConverterCalculatorDatabase(driver).currencyQueries,
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
    fun getCurrenciesFlow() = runTest {
        val currencies = subject.getCurrenciesFlow().first()

        assertTrue { currencies.isNotEmpty() }
        assertEquals(currencies.sortedBy { it.code }, currencies)
    }

    @Test
    fun getActiveCurrenciesFlow() = runTest {
        assertTrue { subject.getActiveCurrenciesFlow().first().isEmpty() }

        subject.updateCurrencyStateByCode(EUR, true)

        assertEquals(
            listOf(EUR),
            subject.getActiveCurrenciesFlow().first().map { it.code }
        )
    }

    @Test
    fun getActiveCurrencies() = runTest {
        assertTrue { subject.getActiveCurrencies().isEmpty() }

        subject.updateCurrencyStateByCode(EUR, true)

        assertEquals(listOf(EUR), subject.getActiveCurrencies().map { it.code })
    }

    @Test
    fun updateCurrencyStateByCode() = runTest {
        subject.updateCurrencyStateByCode(EUR, true)

        assertTrue { subject.getCurrencyByCode(EUR)?.isActive == true }

        subject.updateCurrencyStateByCode(EUR, false)

        assertFalse { subject.getCurrencyByCode(EUR)?.isActive == true }
    }

    @Test
    fun updateCurrencyStates() = runTest {
        subject.updateCurrencyStates(true)

        assertEquals(
            subject.getCurrenciesFlow().first().size,
            subject.getActiveCurrencies().size
        )

        subject.updateCurrencyStates(false)

        assertTrue { subject.getActiveCurrencies().isEmpty() }
    }

    @Test
    fun getCurrencyByCode() = runTest {
        val currency = subject.getCurrencyByCode(EUR)

        assertEquals(EUR, currency?.code)
        assertEquals("Euro", currency?.name)
        assertFalse { currency?.isActive == true }
    }

    @Test
    fun getCurrencyByCodeReturnsNullForUnknownCode() = runTest {
        assertNull(subject.getCurrencyByCode("UNKNOWN"))
    }

    companion object {
        private const val EUR = "EUR"
    }
}

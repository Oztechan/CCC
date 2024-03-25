package com.oztechan.ccc.android.viewmodel.widget

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days

internal class WidgetViewModelTest {

    private val viewModel: WidgetViewModel by lazy {
        WidgetViewModel(
            calculationStorage = calculationStorage,
            backendApiService = backendApiService,
            currencyDataSource = currencyDataSource,
            appStorage = appStorage
        )
    }

    @Mock
    private val calculationStorage =
        configure(mock(classOf<CalculationStorage>())) { stubsUnitByDefault = true }

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private val base = "EUR"
    private val firstBase = "USD"
    private val lastBase = "TRY"

    private val activeCurrencyList = listOf(
        Currency(code = firstBase, name = "Dollar", symbol = "$", isActive = true),
        Currency(code = base, name = "Euro", symbol = "€", isActive = true),
        Currency(code = lastBase, name = "Turkish Lira", symbol = "₺", isActive = true)
    )

    private val conversion =
        Conversion(base = base, eur = 1.111111, usd = 2.222222, `try` = 3.333333)

    @BeforeTest
    fun setup() {
        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        Logger.setLogWriters(CommonWriter())

        val mockEndDate = Random.nextLong()

        every { appStorage.premiumEndDate }
            .returns(mockEndDate)

        every { calculationStorage.currentBase }
            .returns(base)

        every { calculationStorage.precision }
            .returns(3)

        runTest {
            coEvery { backendApiService.getConversion(base) }
                .returns(conversion)

            coEvery { currencyDataSource.getActiveCurrencies() }
                .returns(activeCurrencyList)
        }
    }

    @Test
    fun `ArrayIndexOutOfBoundsException never thrown`() = runTest {
        // first currency
        every { calculationStorage.currentBase }
            .returns(firstBase)

        coEvery { backendApiService.getConversion(firstBase) }
            .returns(conversion)

        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onRefreshClick()
        }
        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onNextClick()
        }
        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onPreviousClick()
        }

        // middle currency
        every { calculationStorage.currentBase }
            .returns(base)

        coEvery { backendApiService.getConversion(base) }
            .returns(conversion)

        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onRefreshClick()
        }
        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onNextClick()
        }
        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onPreviousClick()
        }

        // last currency
        every { calculationStorage.currentBase }
            .returns(lastBase)

        coEvery { backendApiService.getConversion(lastBase) }
            .returns(conversion)

        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onRefreshClick()
        }
        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onNextClick()
        }
        repeat(activeCurrencyList.count() + 1) {
            viewModel.event.onPreviousClick()
        }
    }

    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertTrue { it.currencyList.isEmpty() }
            assertEquals("", it.lastUpdate)
            assertEquals(base, it.currentBase)
            assertEquals(appStorage.premiumEndDate.isNotPassed(), it.isPremium)
        }
    }

    @Test
    fun `init updates data correctly`() {
        assertNotNull(viewModel.data)
    }

    @Test
    fun `if user is premium api call and db query are invoked`() = runTest {
        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        coVerify { backendApiService.getConversion(base) }
            .wasInvoked()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasInvoked()
    }

    @Test
    fun `if user is not premium no api call and db query are not invoked`() = runTest {
        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        coVerify { backendApiService.getConversion(base) }
            .wasNotInvoked()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasNotInvoked()
    }

    @Test
    fun `when onRefreshClick called all the conversion rates for currentBase is calculated`() =
        runTest {
            every { appStorage.premiumEndDate }
                .returns(nowAsLong() + 1.days.inWholeMilliseconds)

            viewModel.state.onSubscription {
                viewModel.event.onRefreshClick()
            }.firstOrNull().let {
                assertNotNull(it)
                it.currencyList.forEach { currency ->
                    conversion.getRateFromCode(currency.code).let { rate ->
                        assertNotNull(rate)
                        assertEquals(rate.getFormatted(calculationStorage.precision), currency.rate)
                    }
                }
            }
        }

    @Test
    fun `when onRefreshClick called with null, base is not updated`() = runTest {
        // to not invoke getFreshWidgetData
        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasNotInvoked()

        verify { calculationStorage.currentBase = any<String>() }
            .wasNotInvoked()
    }

    // region Event
    @Test
    fun onNextClick() = runTest {
        // when onNextClick, base is updated next or the first active currency
        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onNextClick()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasInvoked()

        verify { calculationStorage.currentBase = lastBase }
            .wasInvoked()

        every { calculationStorage.currentBase }
            .returns(lastBase)

        viewModel.event.onNextClick()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasInvoked()

        verify { calculationStorage.currentBase = firstBase }
            .wasInvoked()
    }

    @Test
    fun onPreviousClick() = runTest {
        // when onRefreshClick, base is updated previous or the last active currency
        every { appStorage.premiumEndDate }
            .returns(nowAsLong() - 1.days.inWholeMilliseconds)

        viewModel.event.onPreviousClick()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasInvoked()

        verify { calculationStorage.currentBase = firstBase }
            .wasInvoked()

        every { calculationStorage.currentBase }
            .returns(firstBase)

        viewModel.event.onPreviousClick()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasInvoked()

        verify { calculationStorage.currentBase = lastBase }
            .wasInvoked()
    }

    @Test
    fun onRefreshClick() = runTest {
        every { appStorage.premiumEndDate }
            .returns(nowAsLong() + 1.days.inWholeMilliseconds)

        viewModel.event.onRefreshClick()

        coVerify { backendApiService.getConversion(base) }
            .wasInvoked()

        coVerify { currencyDataSource.getActiveCurrencies() }
            .wasInvoked()

        verify { calculationStorage.currentBase }
            .wasInvoked()
    }

    @Test
    fun onOpenAppClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onOpenAppClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WidgetEffect.OpenApp>(it)
        }
    }
    // endregion
}

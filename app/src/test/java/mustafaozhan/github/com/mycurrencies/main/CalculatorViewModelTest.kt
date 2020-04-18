package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.rule.CoroutineTestRule
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalculatorViewModelTest {

    private lateinit var viewModel: CalculatorViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @MockK
    lateinit var backendRepository: BackendRepository

    @RelaxedMockK
    lateinit var currencyRepository: CurrencyRepository

    @MockK
    lateinit var offlineRatesRepository: OfflineRatesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = CalculatorViewModel(
            preferencesRepository,
            backendRepository,
            currencyRepository,
            offlineRatesRepository
        )
    }

    @Test
    fun `is live data emitting`() {

        // state
        val mockInput = "1"
        val mockBase = "EUR"
        val mockCurrencyList: MutableList<Currency> = mutableListOf()
        val mockOutput = "123.45"
        val mockSymbol = "$"
        val mockLoading = false

        viewModel.state.apply {
            input.postValue(mockInput)
            base.postValue(mockBase)
            currencyList.postValue(mockCurrencyList)
            output.postValue(mockOutput)
            symbol.postValue(mockSymbol)
            loading.postValue(mockLoading)

            assertEquals(input.value, mockInput)
            assertEquals(base.value, mockBase)
            assertEquals(currencyList.value, mockCurrencyList)
            assertEquals(output.value, mockOutput)
            assertEquals(symbol.value, mockSymbol)
            assertEquals(loading.value, mockLoading)
        }
    }
}

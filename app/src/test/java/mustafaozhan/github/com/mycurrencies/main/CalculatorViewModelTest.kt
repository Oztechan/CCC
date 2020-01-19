package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.CalculatorViewState
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

    @MockK
    lateinit var preferencesRepository: PreferencesRepository
    @MockK
    lateinit var backendRepository: BackendRepository
    @MockK
    lateinit var currencyDao: CurrencyDao
    @MockK
    lateinit var offlineRatesDao: OfflineRatesDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        preferencesRepository = mockk()
        backendRepository = mockk()
        currencyDao = mockk()
        offlineRatesDao = mockk()
        viewModel = CalculatorViewModel(preferencesRepository, backendRepository, currencyDao, offlineRatesDao)
    }

    @Test
    fun `is view model initialized successfully`() {
        assertEquals(viewModel.onLoaded(), Completable.complete())
    }

    @Test
    fun `is live data emitting`() {
        val output = "123.45"
        val currencyList: MutableList<Currency> = mutableListOf()

        val date = "12:34:56 01.01.2020"
        val rates = Rates("EUR", date)
        val calculatorViewState = CalculatorViewState.Success(rates)

        viewModel.outputLiveData.postValue(output)
        viewModel.currencyListLiveData.postValue(currencyList)
        viewModel.calculatorViewStateLiveData.postValue(calculatorViewState)

        assertEquals(viewModel.outputLiveData.value, output)
        assertEquals(viewModel.currencyListLiveData.value, currencyList)
        assertEquals(viewModel.calculatorViewStateLiveData.value, calculatorViewState)
    }
}

package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @RelaxedMockK
    lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel(preferencesRepository, currencyRepository)
    }

    @Test
    fun `is live data emitting`() {

        // state
        val mockCurrencyList: MutableList<Currency> = mutableListOf()
        val mockSearchQuery = "abc"

        viewModel.state.apply {

            currencyList.postValue(mockCurrencyList)
            searchQuery.postValue(mockSearchQuery)

            Assert.assertEquals(currencyList.value, mockCurrencyList)
            Assert.assertEquals(searchQuery.value, mockSearchQuery)
        }
    }
}

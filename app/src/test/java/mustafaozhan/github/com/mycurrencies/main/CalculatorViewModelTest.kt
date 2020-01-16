package mustafaozhan.github.com.mycurrencies.main

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import mustafaozhan.github.com.mycurrencies.data.repository.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.CalculatorViewModel
import org.junit.Before

class CalculatorViewModelTest {

    lateinit var subject: CalculatorViewModel
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
        subject = CalculatorViewModel(preferencesRepository, backendRepository, currencyDao, offlineRatesDao)
    }
}

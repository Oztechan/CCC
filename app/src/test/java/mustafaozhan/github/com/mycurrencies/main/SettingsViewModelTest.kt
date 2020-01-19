package mustafaozhan.github.com.mycurrencies.main

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel

    @MockK
    lateinit var preferencesRepository: PreferencesRepository
    @MockK
    lateinit var currencyDao: CurrencyDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel(preferencesRepository, currencyDao)
    }

    @Test
    fun `is view model initialized successfully`() {
        assertEquals(viewModel.onLoaded(), Completable.complete())
    }
}

// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.main

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorViewModel
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalculatorViewModelTest {

    private lateinit var viewModel: CalculatorViewModel

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
}

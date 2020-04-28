// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.extension.getCurrencyConversionByRate
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData.Companion.KEY_AC
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorData.Companion.KEY_DEL
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.LongClickEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.model.ReverseSpinner
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
open class CalculatorViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CalculatorViewModel

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @MockK
    lateinit var backendRepository: BackendRepository

    @RelaxedMockK
    lateinit var currencyRepository: CurrencyRepository

    @MockK
    lateinit var offlineRatesRepository: OfflineRatesRepository

    private lateinit var event: CalculatorEvent

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = CalculatorViewModel(
            preferencesRepository,
            backendRepository,
            currencyRepository,
            offlineRatesRepository
        )
        event = viewModel.getEvent()
    }

    @Test
    fun `spinner item click`() {
        val clickedItem = "asd"
        event.onSpinnerItemSelected(clickedItem)
        Assert.assertEquals(clickedItem, viewModel.state.base.value)
    }

    @Test
    fun `bar click`() {
        event.onBarClick()
        Assert.assertEquals(ReverseSpinner, viewModel.effect.value)
    }

    @Test
    fun `on item click`() {
        val currency = Currency("USD", "Dollar", "$", 0.0, 1)
        val conversion = "123.456"
        event.onItemClick(currency, conversion)

        Assert.assertEquals(currency.name, viewModel.state.base.value)
        Assert.assertEquals(conversion, viewModel.state.input.value)

        val unValidConversion = "123."
        val validConversion = "123"
        event.onItemClick(currency, unValidConversion)
        Assert.assertEquals(validConversion, viewModel.state.input.value)
    }

    @Test
    fun `on item long click`() {
        val currency = Currency("USD", "Dollar", "$", 0.0, 1)

        event.onItemLongClick(currency)

        Assert.assertEquals(
            LongClickEffect(
                currency.getCurrencyConversionByRate(preferencesRepository.currentBase, viewModel.data.rates),
                currency.name
            ),
            viewModel.effect.value
        )
    }

    @Test
    fun `on key press`() {
        val oldValue = viewModel.state.input.value
        val key = "1"
        event.onKeyPress(key)
        Assert.assertEquals(oldValue + key, viewModel.state.input.value)

        event.onKeyPress(KEY_AC)
        Assert.assertEquals("", viewModel.state.input.value)

        val currentInput = "12345"
        event.onKeyPress(currentInput)
        event.onKeyPress(KEY_DEL)
        Assert.assertEquals(currentInput.dropLast(1), viewModel.state.input.value)
    }
}

/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.newSingleThreadContext
import mustafaozhan.github.com.mycurrencies.data.api.ApiRepository
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.db.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorData.Companion.KEY_AC
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorData.Companion.KEY_DEL
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorEvent
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.OpenBarEffect
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.ShowRateEffect
import mustafaozhan.github.com.mycurrencies.util.extension.getCurrencyConversionByRate
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
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var viewModel: CalculatorViewModel

    @RelaxedMockK
    lateinit var preferencesRepository: PreferencesRepository

    @MockK
    lateinit var apiRepository: ApiRepository

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    @MockK
    lateinit var offlineRatesDao: OfflineRatesDao

    private lateinit var event: CalculatorEvent

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = CalculatorViewModel(
            preferencesRepository,
            apiRepository,
            currencyDao,
            offlineRatesDao
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
        Assert.assertEquals(OpenBarEffect, viewModel.effect.value)
    }

    @Test
    fun `on item click`() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
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
        val currency = Currency("USD", "Dollar", "$", 0.0, true)

        event.onItemLongClick(currency)

        Assert.assertEquals(
            ShowRateEffect(
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

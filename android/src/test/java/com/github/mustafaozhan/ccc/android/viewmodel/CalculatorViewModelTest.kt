/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.viewmodel

import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorViewModel.Companion.KEY_AC
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorViewModel.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.android.ui.calculator.OpenBarEffect
import com.github.mustafaozhan.ccc.android.ui.calculator.OpenSettingsEffect
import com.github.mustafaozhan.ccc.android.ui.calculator.ShowRateEffect
import com.github.mustafaozhan.ccc.android.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.data.db.OfflineRatesDao
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override lateinit var viewModel: CalculatorViewModel

    @RelaxedMockK
    lateinit var settingsRepository: SettingsRepository

    @MockK
    lateinit var apiRepository: ApiRepository

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    @MockK
    lateinit var offlineRatesDao: OfflineRatesDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CalculatorViewModel(
            settingsRepository,
            apiRepository,
            currencyDao,
            offlineRatesDao
        )
    }

    // Event
    @Test
    fun `spinner item click`() = with(viewModel) {
        val clickedItem = "asd"
        getEvent().onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, state.base.value)
    }

    @Test
    fun `bar click`() = with(viewModel) {
        getEvent().onBarClick()
        assertEquals(OpenBarEffect, effect.value)
    }

    @Test
    fun `settings click`() = with(viewModel) {
        getEvent().onSettingsClicked()
        assertEquals(OpenSettingsEffect, effect.value)
    }

    @Test
    fun `on item click`() = with(viewModel) {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val conversion = "123.456"
        getEvent().onItemClick(currency, conversion)

        assertEquals(currency.name, state.base.value)
        assertEquals(conversion, state.input.value)

        val unValidConversion = "123."
        val validConversion = "123"
        getEvent().onItemClick(currency, unValidConversion)
        assertEquals(validConversion, state.input.value)
    }

    @Test
    fun `on item long click`() = with(viewModel) {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)

        getEvent().onItemLongClick(currency)

        assertEquals(
            ShowRateEffect(
                currency.getCurrencyConversionByRate(settingsRepository.currentBase, data.rates),
                currency.name
            ),
            effect.value
        )
    }

    @Test
    fun `on key press`() = with(viewModel) {
        val oldValue = state.input.value
        val key = "1"
        getEvent().onKeyPress(key)
        assertEquals(oldValue + key, state.input.value)

        getEvent().onKeyPress(KEY_AC)
        assertEquals("", state.input.value)

        val currentInput = "12345"
        getEvent().onKeyPress(currentInput)
        getEvent().onKeyPress(KEY_DEL)
        assertEquals(currentInput.dropLast(1), state.input.value)
    }
}

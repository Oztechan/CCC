/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.client.runTest
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenBarEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenSettingsEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.ShowRateEffect
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.common.api.ApiFactory
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.model.Currency
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override lateinit var viewModel: CalculatorViewModel

    @BeforeTest
    fun setup() {
        viewModel = CalculatorViewModel(
            SettingsRepository(this),
            ApiRepository(ApiFactory()),
            CurrencyDao(this),
            OfflineRatesDao(this)
        )
    }

    // Event
    @Test
    fun onSpinnerItemSelected() = with(viewModel) {
        val clickedItem = "asd"
        getEvent().onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, state.base.value)
    }

    @Test
    fun onBarClick() = runTest {
        it.launch {
            viewModel.getEvent().onBarClick()

            assertEquals(OpenBarEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onSettingsClicked() = runTest {
        it.launch {
            viewModel.getEvent().onSettingsClicked()
            assertEquals(OpenSettingsEffect, viewModel.effect.single())
        }.cancel()
    }

    @Test
    fun onItemClick() = with(viewModel) {
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
    fun onItemLongClick() = runTest {
        it.launch {
            val currency = Currency("USD", "Dollar", "$", 0.0, true)

            viewModel.getEvent().onItemLongClick(currency)

            assertEquals(
                ShowRateEffect(
                    currency.getCurrencyConversionByRate(
                        viewModel.getCurrentBase(),
                        viewModel.data.rates
                    ),
                    currency.name
                ),
                viewModel.effect.single()
            )
        }.cancel()
    }

    @Test
    fun onKeyPress() = with(viewModel) {
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

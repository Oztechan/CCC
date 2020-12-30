/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.OpenBarEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.OpenSettingsEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.ShowRateEffect
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override val viewModel: CalculatorViewModel by lazy {
        koin.getDependency(CalculatorViewModel::class)
    }

    // Event
    @Test
    fun onSpinnerItemSelected() = with(viewModel) {
        val clickedItem = "asd"
        getEvent().onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, state.value.base)
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

        assertEquals(currency.name, state.value.base)
        assertEquals(conversion, state.value.input)

        val unValidConversion = "123."
        val validConversion = "123"
        getEvent().onItemClick(currency, unValidConversion)
        assertEquals(validConversion, state.value.input)
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
        val oldValue = state.value.input
        val key = "1"
        getEvent().onKeyPress(key)
        assertEquals(oldValue + key, state.value.input)

        getEvent().onKeyPress(KEY_AC)
        assertEquals("", state.value.input)

        val currentInput = "12345"
        getEvent().onKeyPress(currentInput)
        getEvent().onKeyPress(KEY_DEL)
        assertEquals(currentInput.dropLast(1), state.value.input)
    }
}

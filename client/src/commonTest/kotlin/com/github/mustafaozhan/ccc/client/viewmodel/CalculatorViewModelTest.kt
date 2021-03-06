/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.viewmodel.CalculatorViewModel.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.CalculatorViewModel.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override val viewModel: CalculatorViewModel by lazy {
        koin.getDependency(CalculatorViewModel::class)
    }

    // Event
    @Test
    fun onSpinnerItemSelected() = with(viewModel) {
        val clickedItem = "asd"
        event.onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, state.value.base)
    }

    @Test
    fun onBarClick() = runTest {
        viewModel.event.onBarClick()

        assertEquals(CalculatorEffect.OpenBar, viewModel.effect.first())
    }

    @Test
    fun onSettingsClicked() = runTest {
        viewModel.event.onSettingsClicked()
        assertEquals(CalculatorEffect.OpenSettings, viewModel.effect.first())
    }

    @Test
    fun onItemClick() = with(viewModel) {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val conversion = "123.456"
        event.onItemClick(currency, conversion)

        assertEquals(currency.name, state.value.base)
        assertEquals(conversion, state.value.input)

        val unValidConversion = "123."
        val validConversion = "123"
        event.onItemClick(currency, unValidConversion)
        assertEquals(validConversion, state.value.input)
    }

    @Test
    fun onItemLongClick() = runTest {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)

        viewModel.event.onItemLongClick(currency)

        assertEquals(
            CalculatorEffect.ShowRate(
                currency.getCurrencyConversionByRate(
                    viewModel.state.value.base,
                    viewModel.data.rates
                ),
                currency.name
            ),
            viewModel.effect.first()
        )
    }

    @Test
    fun onKeyPress() = with(viewModel) {
        val oldValue = state.value.input
        val key = "1"
        event.onKeyPress(key)
        assertEquals(oldValue + key, state.value.input)

        event.onKeyPress(KEY_AC)
        assertEquals("", state.value.input)

        val currentInput = "12345"
        event.onKeyPress(currentInput)
        event.onKeyPress(KEY_DEL)
        assertEquals(currentInput.dropLast(1), state.value.input)
    }
}

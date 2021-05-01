/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.test
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CalculatorViewModelTest : BaseViewModelTest<CalculatorViewModel>() {

    override val viewModel: CalculatorViewModel by lazy {
        koin.getDependency(CalculatorViewModel::class)
    }

    private val currency = Currency("USD", "Dollar", "$", 12345.6789, true)

    // Event
    @Test
    fun onSpinnerItemSelected() = with(viewModel) {
        val clickedItem = "EUR"
        event.onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, state.value.base)
    }

    @Test
    fun onBarClick() = viewModel.effect.test({
        viewModel.event.onBarClick()
    }, {
        assertEquals(CalculatorEffect.OpenBar, it)
    })

    @Test
    fun onSettingsClicked() = viewModel.effect.test({
        viewModel.event.onSettingsClicked()
    }, {
        assertEquals(CalculatorEffect.OpenSettings, it)
    })

    @Test
    fun onItemClick() = viewModel.effect.test({
        viewModel.event.onItemClick(currency)
    }, {
        assertEquals(currency.name, viewModel.state.value.base)
        assertEquals(currency.rate.toString(), viewModel.state.value.input)
    })

    @Test
    fun onItemLongClick() = viewModel.effect.test({
        viewModel.event.onItemLongClick(currency)
    }, {
        assertEquals(
            CalculatorEffect.ShowRate(
                currency.getCurrencyConversionByRate(
                    viewModel.state.value.base,
                    viewModel.data.rates
                ),
                currency.name
            ),
            it
        )
    })

    @Test
    fun onKeyPress() = with(viewModel) {
        val key = "1"

        event.onKeyPress(key)
        assertEquals(key, state.value.input)

        event.onKeyPress(KEY_AC)
        assertEquals("", state.value.input)

        event.onKeyPress(key)
        event.onKeyPress(key)
        event.onKeyPress(KEY_DEL)
        assertEquals(key, state.value.input)
    }

    @Test
    fun onBaseChanged() {
        viewModel.event.onBaseChange(currency.name)
        assertNull(viewModel.data.rates)
        assertEquals(currency.name, viewModel.state.value.base)
    }
}

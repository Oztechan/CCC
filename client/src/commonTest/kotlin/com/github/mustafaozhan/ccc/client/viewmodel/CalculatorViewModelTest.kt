/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    fun onSpinnerItemSelected() = runTest {
        val clickedItem = "asd"
        viewModel.event.onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, viewModel.state.first().base)
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
    fun onItemClick() = runTest {
        with(viewModel) {
            event.onItemClick(currency)
            state.first().let {
                assertEquals(currency.name, state.first().base)
                assertEquals(currency.rate.toString(), state.first().input)
            }
        }
    }

    @Test
    fun onItemLongClick() = runTest {
        viewModel.event.onItemLongClick(currency)
        assertEquals(
            CalculatorEffect.ShowRate(
                currency.getCurrencyConversionByRate(
                    viewModel.state.first().base,
                    viewModel.data.rates
                ),
                currency.name
            ),
            viewModel.effect.first()
        )
    }

    @Test
    fun onKeyPress() = runTest {
        with(viewModel) {
            val oldValue = state.first().input
            val key = "1"
            event.onKeyPress(key)
            assertEquals(oldValue + key, state.first().input)

            event.onKeyPress(KEY_AC)
            assertEquals("", state.first().input)

            event.onKeyPress(key)
            event.onKeyPress(key)
            event.onKeyPress(KEY_DEL)
            delay(200)
            assertEquals(key, state.first().input)
        }
    }

    @Test
    fun onBaseChanged() = runTest {
        viewModel.event.onBaseChange(currency.name)
        assertNull(viewModel.data.rates)
        assertEquals(currency.name, viewModel.state.first().base)
    }
}

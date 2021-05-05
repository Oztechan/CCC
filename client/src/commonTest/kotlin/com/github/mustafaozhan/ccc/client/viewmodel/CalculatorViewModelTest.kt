/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
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
    fun onSpinnerItemSelected() = viewModel.state.before {
        viewModel.event.onSpinnerItemSelected(currency.name)
    }.after {
        assertEquals(currency.name, it?.base)
    }

    @Test
    fun onBarClick() = viewModel.effect.before {
        viewModel.event.onBarClick()
    }.after {
        assertEquals(CalculatorEffect.OpenBar, it)
    }

    @Test
    fun onSettingsClicked() = viewModel.effect.before {
        viewModel.event.onSettingsClicked()
    }.after {
        assertEquals(CalculatorEffect.OpenSettings, it)
    }

    @Test
    fun onItemClick() = viewModel.state.before {
        viewModel.event.onItemClick(currency)
    }.after {
        assertEquals(currency.name, it?.base)
        assertEquals(currency.rate.toString(), it?.input)
    }

    @Test
    fun onItemLongClick() = viewModel.effect.before {
        viewModel.event.onItemLongClick(currency)
    }.after {
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
    }

    @Test
    fun onKeyPress() = with(viewModel) {
        val key = "1"

        state.before {
            event.onKeyPress(key)
        }.after {
            assertEquals(key, it?.input)
        }

        state.before {
            event.onKeyPress(KEY_AC)
        }.after {
            assertEquals("", it?.input)
        }

        state.before {
            event.onKeyPress(key)
            event.onKeyPress(key)
            event.onKeyPress(KEY_DEL)
        }.after {
            assertEquals(key, it?.input)
        }
    }

    @Test
    fun onBaseChanged() = viewModel.state.before {
        viewModel.onBaseChange(currency.name)
    }.after {
        assertNull(viewModel.data.rates)
        assertEquals(currency.name, it?.base)
    }
}

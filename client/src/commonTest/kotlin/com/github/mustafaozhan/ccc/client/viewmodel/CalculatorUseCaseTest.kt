/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseUseCaseTest
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorUseCase
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorUseCase.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorUseCase.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenBarEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.OpenSettingsEffect
import com.github.mustafaozhan.ccc.client.ui.calculator.ShowRateEffect
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class CalculatorUseCaseTest : BaseUseCaseTest<CalculatorUseCase>() {

    override val useCase: CalculatorUseCase by lazy {
        koin.getDependency(CalculatorUseCase::class)
    }

    // Event
    @Test
    fun onSpinnerItemSelected() = with(useCase) {
        val clickedItem = "asd"
        getEvent().onSpinnerItemSelected(clickedItem)
        assertEquals(clickedItem, state.base.value)
    }

    @Test
    fun onBarClick() = runTest {
        it.launch {
            useCase.getEvent().onBarClick()

            assertEquals(OpenBarEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onSettingsClicked() = runTest {
        it.launch {
            useCase.getEvent().onSettingsClicked()
            assertEquals(OpenSettingsEffect, useCase.effect.single())
        }.cancel()
    }

    @Test
    fun onItemClick() = with(useCase) {
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

            useCase.getEvent().onItemLongClick(currency)

            assertEquals(
                ShowRateEffect(
                    currency.getCurrencyConversionByRate(
                        useCase.getCurrentBase(),
                        useCase.data.rates
                    ),
                    currency.name
                ),
                useCase.effect.single()
            )
        }.cancel()
    }

    @Test
    fun onKeyPress() = with(useCase) {
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

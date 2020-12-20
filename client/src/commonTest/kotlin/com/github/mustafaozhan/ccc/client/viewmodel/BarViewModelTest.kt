/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.runTest
import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.common.CurrencyQueries
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.model.Currency
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class BarViewModelTest : BaseViewModelTest<BarViewModel>(), CurrencyQueries {

    override lateinit var viewModel: BarViewModel

    @BeforeTest
    fun setup() {
        viewModel = BarViewModel(CurrencyDao(this))
    }

    @Test
    fun onItemClick() = runTest {
        it.launch {
            val currency = Currency("USD", "Dollar", "$", 0.0, true)
            viewModel.getEvent().onItemClick(currency)

            assertEquals(
                ChangeBaseNavResultEffect(currency.name),
                viewModel.effect.single()
            )
        }.cancel()
    }

    @Test
    fun onSelectClick() = runTest {
        it.launch {
            viewModel.getEvent().onSelectClick()
            assertEquals(OpenCurrenciesEffect, viewModel.effect.single())
        }.cancel()
    }
}

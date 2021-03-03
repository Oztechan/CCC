/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals

class BarViewModelTest : BaseViewModelTest<BarViewModel>() {

    override val viewModel: BarViewModel by lazy {
        koin.getDependency(BarViewModel::class)
    }

    @Test
    fun onItemClick() = runTest {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        viewModel.event.onItemClick(currency)

        assertEquals(
            BarEffect.ChangeBase(currency.name),
            viewModel.effect.first()
        )
    }

    @Test
    fun onSelectClick() = runTest {
        viewModel.event.onSelectClick()
        assertEquals(BarEffect.OpenCurrencies, viewModel.effect.first())
    }
}

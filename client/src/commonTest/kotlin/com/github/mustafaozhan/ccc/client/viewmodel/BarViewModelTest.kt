/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first

class BarViewModelTest : BaseViewModelTest<BarViewModel>() {

    override val viewModel: BarViewModel by lazy {
        koin.getDependency(BarViewModel::class)
    }

    @Test
    fun onItemClick() = runTest {
        kermit.d { "BarViewModelTest onItemClick inside test" }
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        viewModel.event.onItemClick(currency)

        kermit.d { "BarViewModelTest onItemClick before asset" }
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

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.viewmodel.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class BarViewModelTest : BaseViewModelTest<BarViewModel>() {

    override val viewModel: BarViewModel by lazy {
        koin.getDependency(SettingsRepository::class)
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

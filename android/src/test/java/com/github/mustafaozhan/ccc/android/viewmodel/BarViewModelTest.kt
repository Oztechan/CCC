/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.viewmodel

import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.bar.OpenCurrenciesEffect
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.model.Currency
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BarViewModelTest : BaseViewModelTest<BarViewModel>() {

    override lateinit var viewModel: BarViewModel

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = BarViewModel(currencyDao)
    }

    // Event
    @Test
    fun `on item click`() = runBlockingTest {
        launch {
            val currency = Currency("USD", "Dollar", "$", 0.0, true)
            viewModel.getEvent().onItemClick(currency)

            assertEquals(
                ChangeBaseNavResultEffect(currency.name),
                viewModel.effect.single()
            )
        }.cancel()
    }

    @Test
    fun `on select click`() = runBlockingTest {
        launch {
            viewModel.getEvent().onSelectClick()
            assertEquals(OpenCurrenciesEffect, viewModel.effect.single())
        }.cancel()
    }
}

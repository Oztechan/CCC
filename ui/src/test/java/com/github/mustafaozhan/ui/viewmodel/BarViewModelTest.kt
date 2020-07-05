/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.viewmodel

import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.ui.main.bar.BarViewModel
import com.github.mustafaozhan.ui.main.bar.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ui.main.bar.OpenSettingsEffect
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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
    fun `on item click`() = with(viewModel) {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        getEvent().onItemClick(currency)

        assertEquals(
            ChangeBaseNavResultEffect(currency.name),
            effect.value
        )
    }

    @Test
    fun `on select click`() = with(viewModel) {
        getEvent().onSelectClick()
        assertEquals(OpenSettingsEffect, effect.value)
    }
}

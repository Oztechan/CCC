/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.viewmodel

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.bar.BarViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.bar.ChangeBaseNavResultEffect
import mustafaozhan.github.com.mycurrencies.ui.main.bar.OpenSettingsEffect
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

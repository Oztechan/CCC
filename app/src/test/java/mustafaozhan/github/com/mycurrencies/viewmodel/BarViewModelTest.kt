/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.rule.TestCoroutineRule
import mustafaozhan.github.com.mycurrencies.ui.main.bar.BarEvent
import mustafaozhan.github.com.mycurrencies.ui.main.bar.BarViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.bar.ChangeBaseNavResultEffect
import mustafaozhan.github.com.mycurrencies.ui.main.bar.OpenSettingsEffect
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BarViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: BarViewModel

    @RelaxedMockK
    lateinit var currencyDao: CurrencyDao

    private lateinit var event: BarEvent

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = BarViewModel(currencyDao)
        event = viewModel.getEvent()
    }

    @Test
    fun `on item click`() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        event.onItemClick(currency)

        assertEquals(
            ChangeBaseNavResultEffect(currency.name),
            viewModel.effect.value
        )
    }

    @Test
    fun `on select click`() {
        event.onSelectClick()
        assertEquals(OpenSettingsEffect, viewModel.effect.value)
    }
}

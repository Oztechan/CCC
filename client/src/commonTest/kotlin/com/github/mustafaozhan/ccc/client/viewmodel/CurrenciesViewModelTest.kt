/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CurrenciesViewModelTest : BaseViewModelTest<CurrenciesViewModel>() {

    override val viewModel: CurrenciesViewModel by lazy {
        koin.getDependency(CurrenciesViewModel::class)
    }

    @Test
    fun hideSelectionVisibility() {
        viewModel.hideSelectionVisibility()
        assertEquals(false, viewModel.state.value.selectionVisibility)
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        viewModel.event.onQueryChange(query)
        assertEquals(query, viewModel.data.query)
    }

    // Event
    @Test
    fun onQueryChange() = runTest {
        val euro = Currency("EUR", "Euro", "€")
        val dollar = Currency("USD", "American Dollar", "$")

        val originalList = mutableListOf<Currency>().apply {
            add(euro)
            add(dollar)
        }

        with(viewModel) {
            data.unFilteredList = originalList
            event.onQueryChange("USD")
            delay(100)
            assertTrue(state.value.currencyList.contains(dollar))

            data.unFilteredList = originalList
            event.onQueryChange("Euro")
            delay(100)
            assertTrue(state.value.currencyList.contains(euro))

            data.unFilteredList = originalList
            event.onQueryChange("$")
            delay(100)
            assertTrue(state.value.currencyList.contains(dollar))

            data.unFilteredList = originalList
            event.onQueryChange("asdasd")
            delay(100)
            assertTrue(state.value.currencyList.isEmpty())

            data.unFilteredList = originalList
            event.onQueryChange("o")
            delay(100)
            assertEquals(2, state.value.currencyList.size)
        }
    }

    @Test
    fun onItemLongClick() = with(viewModel) {
        val currentValue = viewModel.state.value.selectionVisibility
        event.onItemLongClick()
        assertEquals(!currentValue, viewModel.state.value.selectionVisibility)
    }

    @Test
    fun onCloseClick() = runTest {
        viewModel.event.onCloseClick()

        // init causes CurrenciesEffect.ChangeBase before
        assertEquals(
            true,
            viewModel.effect
                .take(2)
                .toList().contains(CurrenciesEffect.Back)
        )

        assertEquals("", viewModel.data.query)
    }

    @Test
    fun onDoneClick() = runTest {
        viewModel.event.onDoneClick()

        // init causes CurrenciesEffect.ChangeBase before
        assertEquals(
            true,
            viewModel.effect
                .take(2)
                .toList().contains(CurrenciesEffect.FewCurrency)
        )
    }
}

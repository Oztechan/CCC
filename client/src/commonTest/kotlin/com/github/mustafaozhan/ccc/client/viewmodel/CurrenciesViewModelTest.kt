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
import kotlinx.coroutines.flow.first
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
    fun hideSelectionVisibility() = runTest {
        viewModel.hideSelectionVisibility()
        assertEquals(false, viewModel.state.first().selectionVisibility)
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
            assertTrue(state.first().currencyList.contains(dollar))

            data.unFilteredList = originalList
            event.onQueryChange("Euro")
            assertTrue(state.first().currencyList.contains(euro))

            data.unFilteredList = originalList
            event.onQueryChange("$")
            assertTrue(state.first().currencyList.contains(dollar))

            data.unFilteredList = originalList
            event.onQueryChange("asdasd")
            assertTrue(state.first().currencyList.isEmpty())

            data.unFilteredList = originalList
            event.onQueryChange("o")
            assertEquals(2, state.first().currencyList.size)
        }
    }

    @Test
    fun onItemLongClick() = runTest {
        with(viewModel) {
            val currentValue = viewModel.state.first().selectionVisibility
            event.onItemLongClick()
            assertEquals(!currentValue, viewModel.state.first().selectionVisibility)
        }
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

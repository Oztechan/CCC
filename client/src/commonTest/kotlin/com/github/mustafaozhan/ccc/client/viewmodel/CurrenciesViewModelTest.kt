/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.test
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
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
            assertTrue(state.value.currencyList.contains(dollar))

            data.unFilteredList = originalList
            event.onQueryChange("Euro")
            assertTrue(state.value.currencyList.contains(euro))

            data.unFilteredList = originalList
            event.onQueryChange("$")
            assertTrue(state.value.currencyList.contains(dollar))

            data.unFilteredList = originalList
            event.onQueryChange("asdasd")
            assertTrue(state.value.currencyList.isEmpty())

            data.unFilteredList = originalList
            event.onQueryChange("o")
            assertEquals(2, state.value.currencyList.size)
        }
    }

    @Test
    fun onItemLongClick() = runTest {
        with(viewModel) {
            val currentValue = viewModel.state.value.selectionVisibility
            event.onItemLongClick()
            assertEquals(!currentValue, viewModel.state.value.selectionVisibility)
        }
    }

    @Test
    fun onCloseClick() = viewModel.effect.test({
        viewModel.event.onCloseClick()
    }, {
        assertTrue { it is CurrenciesEffect.Back }
        assertEquals("", viewModel.data.query)
    })

    @Test
    fun onDoneClick() = viewModel.effect.test({
        viewModel.event.onDoneClick()
    }, {
        assertTrue { it is CurrenciesEffect.FewCurrency }
    })
}

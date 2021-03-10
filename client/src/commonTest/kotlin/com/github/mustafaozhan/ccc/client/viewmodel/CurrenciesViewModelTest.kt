/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CurrenciesViewModelTest : BaseViewModelTest<CurrenciesViewModel>() {

    override val viewModel: CurrenciesViewModel by lazy {
        koin.getDependency(CurrenciesViewModel::class)
    }

    @Test
    fun filterList() {
        val euro = Currency("EUR", "Euro", "€")
        val dollar = Currency("USD", "American Dollar", "$")

        val originalList = mutableListOf<Currency>().apply {
            add(euro)
            add(dollar)
        }

        with(viewModel) {
            data.unFilteredList = originalList
            filterList("USD").run {
                assertTrue(state.value.currencyList.contains(dollar))
            }
            data.unFilteredList = originalList
            filterList("Euro").run {
                assertTrue(state.value.currencyList.contains(euro))
            }
            data.unFilteredList = originalList
            filterList("$").run {
                assertTrue(state.value.currencyList.contains(dollar))
            }
            data.unFilteredList = originalList
            filterList("asdasd").run {
                assertTrue(state.value.currencyList.isEmpty())
            }
            data.unFilteredList = originalList
            filterList("o").run {
                assertEquals(2, state.value.currencyList.size)
            }
        }
    }

    @Test
    fun hideSelectionVisibility() {
        viewModel.hideSelectionVisibility().run {
            assertEquals(false, viewModel.state.value.selectionVisibility)
        }
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        viewModel.filterList(query).run {
            assertEquals(query, viewModel.data.query)
        }
    }

    // Event
    @Test
    fun onItemLongClick() = with(viewModel) {
        val currentValue = viewModel.state.value.selectionVisibility
        event.onItemLongClick().run {
            assertEquals(!currentValue, viewModel.state.value.selectionVisibility)
        }
    }
//    @Test
//    fun onCloseClick() = runTest {
//        viewModel.event.onCloseClick()
//        assertEquals(CurrenciesEffect.Back, viewModel.effect.first())
//        assertEquals("", viewModel.data.query)
//    }
//
//    @Test
//    fun onDoneClick() = runTest {
//        viewModel.event.onDoneClick()
//        assertEquals(CurrenciesEffect.FewCurrency, viewModel.effect.first())
//    }
}

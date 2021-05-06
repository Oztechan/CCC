/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CurrenciesViewModelTest : BaseViewModelTest<CurrenciesViewModel>() {

    override val viewModel: CurrenciesViewModel by lazy {
        koin.getDependency(CurrenciesViewModel::class)
    }

    @Test
    fun hideSelectionVisibility() = viewModel.state.before {
        viewModel.hideSelectionVisibility()
    }.after {
        assertEquals(false, it?.selectionVisibility)
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        viewModel.event.onQueryChange(query)
        assertEquals(query, viewModel.data.query)
    }

    // Event
    @Test
    fun onQueryChange() = with(viewModel) {
        val euro = Currency("EUR", "Euro", "€")
        val dollar = Currency("USD", "American Dollar", "$")

        val originalList = mutableListOf<Currency>().apply {
            add(euro)
            add(dollar)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("USD")
        }.after {
            assertEquals(true, it?.currencyList?.contains(dollar))
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("Euro")
        }.after {
            assertEquals(true, it?.currencyList?.contains(euro))
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("$")
        }.after {
            assertEquals(true, it?.currencyList?.contains(dollar))
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("something")
        }.after {
            assertEquals(true, it?.currencyList?.isEmpty())
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("o")
        }.after {
            assertEquals(2, it?.currencyList?.size)
        }
    }

    @Test
    fun onItemLongClick() = viewModel.state.before {
        viewModel.event.onItemLongClick()
    }.after {
        assertEquals(true, it?.selectionVisibility)
    }

    @Test
    fun onCloseClick() = viewModel.effect.before {
        viewModel.onCloseClick()
    }.after {
        assertTrue { it is CurrenciesEffect.Back }
        assertEquals("", viewModel.data.query)
    }

    @Test
    fun onDoneClick() = viewModel.effect.before {
        viewModel.onDoneClick()
    }.after {
        assertTrue { it is CurrenciesEffect.FewCurrency }
    }
}

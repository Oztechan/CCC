/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.ui.currencies.BackEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.ui.currencies.FewCurrencyEffect
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class CurrenciesViewModelTest : BaseViewModelTest<CurrenciesViewModel>() {

    override val viewModel: CurrenciesViewModel by lazy {
        koin.getDependency(CurrenciesViewModel::class)
    }

    @Test
    fun filterList() {
        val euro = Currency("EUR", "Euro", "€")
        val dollar = Currency("USD", "American Dollar", "$")

        viewModel.data.unFilteredList = mutableListOf<Currency>().apply {
            add(euro)
            add(dollar)
        }

        viewModel.filterList("USD")
        assertEquals(true, viewModel.state.currencyList.value.contains(dollar))

        viewModel.filterList("Euro")
        assertEquals(true, viewModel.state.currencyList.value.contains(euro))

        viewModel.filterList("$")
        assertEquals(true, viewModel.state.currencyList.value.contains(dollar))

        viewModel.filterList("asdasd")
        assertEquals(true, viewModel.state.currencyList.value.isEmpty())

        viewModel.filterList("o")
        assertEquals(2, viewModel.state.currencyList.value.size)
    }

    @Test
    fun hideSelectionVisibility() {
        viewModel.hideSelectionVisibility()
        assertEquals(false, viewModel.state.selectionVisibility.value)
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        viewModel.filterList(query)
        assertEquals(query, viewModel.data.query)
    }

    // Event
    @Test
    fun onItemLongClick() = with(viewModel) {
        val currentValue = viewModel.state.selectionVisibility.value
        getEvent().onItemLongClick()
        assertEquals(!currentValue, viewModel.state.selectionVisibility.value)
    }

    @Test
    fun updateAllCurrenciesState() {
        assertEquals(Unit, viewModel.getEvent().updateAllCurrenciesState(true))
        assertEquals(Unit, viewModel.getEvent().updateAllCurrenciesState(false))
    }

    @Test
    fun onItemClick() {
        val currency = Currency("EUR", "Euro", "€")
        assertEquals(Unit, viewModel.getEvent().onItemClick(currency))
    }

    @Test
    fun onCloseClick() = runTest {
        it.launch {
            viewModel.getEvent().onCloseClick()
            assertEquals(BackEffect, viewModel.effect.single())
            assertEquals("", viewModel.data.query)
        }.cancel()
    }

    @Test
    fun onDoneClick() = runTest {
        it.launch {
            viewModel.getEvent().onDoneClick()
            assertEquals(FewCurrencyEffect, viewModel.effect.single())
        }.cancel()
    }
}

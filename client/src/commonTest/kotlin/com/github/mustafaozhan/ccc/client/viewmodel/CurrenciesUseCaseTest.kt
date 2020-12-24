/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseUseCaseTest
import com.github.mustafaozhan.ccc.client.ui.currencies.BackEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesUseCase
import com.github.mustafaozhan.ccc.client.ui.currencies.FewCurrencyEffect
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class CurrenciesUseCaseTest : BaseUseCaseTest<CurrenciesUseCase>() {

    override val useCase: CurrenciesUseCase by lazy {
        koin.getDependency(CurrenciesUseCase::class)
    }

    @Test
    fun filterList() {
        val euro = Currency("EUR", "Euro", "€")
        val dollar = Currency("USD", "American Dollar", "$")

        useCase.data.unFilteredList = mutableListOf<Currency>().apply {
            add(euro)
            add(dollar)
        }

        useCase.filterList("USD")
        assertTrue(useCase.state.currencyList.value.contains(dollar))

        useCase.filterList("Euro")
        assertTrue(useCase.state.currencyList.value.contains(euro))

        useCase.filterList("$")
        assertTrue(useCase.state.currencyList.value.contains(dollar))

        useCase.filterList("asdasd")
        assertTrue(useCase.state.currencyList.value.isEmpty())

        useCase.filterList("o")
        assertEquals(2, useCase.state.currencyList.value.size)
    }

    @Test
    fun hideSelectionVisibility() {
        useCase.hideSelectionVisibility()
        assertEquals(false, useCase.state.selectionVisibility.value)
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        useCase.filterList(query)
        assertEquals(query, useCase.data.query)
    }

    // Event
    @Test
    fun onItemLongClick() = with(useCase) {
        val currentValue = useCase.state.selectionVisibility.value
        getEvent().onItemLongClick()
        assertEquals(!currentValue, useCase.state.selectionVisibility.value)
    }

    @Test
    fun updateAllCurrenciesState() {
        assertEquals(Unit, useCase.getEvent().updateAllCurrenciesState(true))
        assertEquals(Unit, useCase.getEvent().updateAllCurrenciesState(false))
    }

    @Test
    fun onItemClick() {
        val currency = Currency("EUR", "Euro", "€")
        assertEquals(Unit, useCase.getEvent().onItemClick(currency))
    }

    @Test
    fun onCloseClick() = runTest {
        it.launch {
            useCase.getEvent().onCloseClick()
            assertEquals(BackEffect, useCase.effect.single())
            assertEquals("", useCase.data.query)
        }.cancel()
    }

    @Test
    fun onDoneClick() = runTest {
        it.launch {
            useCase.getEvent().onDoneClick()
            assertEquals(FewCurrencyEffect, useCase.effect.single())
        }.cancel()
    }
}

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.selectcurrency

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.oztechan.ccc.common.core.model.Currency as CurrencyCommon

internal class SelectCurrencyViewModelTest {

    private val viewModel: SelectCurrencyViewModel by lazy {
        SelectCurrencyViewModel(currencyDataSource)
    }

    private val currencyDataSource = mock<CurrencyDataSource>()

    private val currencyDollar = CurrencyCommon("USD", "Dollar", "$", "", true)
    private val currencyEuro = CurrencyCommon("Eur", "Euro", "", "", true)

    private val currencyListNotEnough = listOf(currencyDollar)
    private val currencyListEnough = listOf(currencyDollar, currencyEuro)

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { currencyDataSource.getActiveCurrenciesFlow() }
            .returns(flowOf(currencyListEnough))
    }

    // SEED
    @Test
    fun `init updates data correctly`() {
        assertFailsWith<RuntimeException> {
            viewModel.data
        }.message.let {
            assertNotNull(it)
            assertEquals("lateinit property data has not been initialized", it)
        }
    }

    // init
    @Test
    fun `init updates the states with no enough currency`() = runTest {
        every { currencyDataSource.getActiveCurrenciesFlow() }
            .returns(flowOf(currencyListNotEnough))

        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.loading }
            assertFalse { it.enoughCurrency }
            assertEquals(currencyListNotEnough, it.currencyList)
        }

        verify { currencyDataSource.getActiveCurrenciesFlow() }
    }

    @Test
    fun `init updates the states with enough currency`() {
        runTest {
            viewModel.state.firstOrNull().let {
                assertNotNull(it)
                assertFalse { it.loading }
                assertTrue { it.enoughCurrency }
                assertEquals(currencyListEnough, it.currencyList)
            }
        }

        verify { currencyDataSource.getActiveCurrenciesFlow() }
    }

    @Test
    fun onItemClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onItemClick(currencyDollar)
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<SelectCurrencyEffect.CurrencyChange>(it)
            assertEquals(currencyDollar.code, it.newBase)
        }
    }

    @Test
    fun onSelectClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onSelectClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<SelectCurrencyEffect.OpenCurrencies>(it)
        }
    }
}

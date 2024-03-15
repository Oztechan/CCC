/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.selectcurrency

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
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
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.oztechan.ccc.common.core.model.Currency as CurrencyCommon

internal class SelectCurrencyViewModelTest {

    private val subject: SelectCurrencyViewModel by lazy {
        SelectCurrencyViewModel(currencyDataSource, calculationStorage)
    }

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val calculationStorage = mock(classOf<CalculationStorage>())

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
        assertNull(subject.data)
    }

    // init
    @Test
    fun `init updates the states with no enough currency`() = runTest {
        every { currencyDataSource.getActiveCurrenciesFlow() }
            .returns(flowOf(currencyListNotEnough))

        subject.state.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.loading }
            assertFalse { it.enoughCurrency }
            assertEquals(currencyListNotEnough, it.currencyList)
        }

        verify { currencyDataSource.getActiveCurrenciesFlow() }
            .wasInvoked()
    }

    @Test
    fun `init updates the states with enough currency`() {
        runTest {
            subject.state.firstOrNull().let {
                assertNotNull(it)
                assertFalse { it.loading }
                assertTrue { it.enoughCurrency }
                assertEquals(currencyListEnough, it.currencyList)
            }
        }

        verify { currencyDataSource.getActiveCurrenciesFlow() }
            .wasInvoked()
    }

    @Test
    fun onItemClick() = runTest {
        subject.effect.onSubscription {
            subject.event.onItemClick(currencyDollar)
        }.firstOrNull().let {
            assertIs<SelectCurrencyEffect.DismissDialog>(it)

            coVerify { calculationStorage.setBase(currencyDollar.code) }
                .wasInvoked()
        }
    }

    @Test
    fun onSelectClick() = runTest {
        subject.effect.onSubscription {
            subject.event.onSelectClick()
        }.firstOrNull().let {
            assertIs<SelectCurrencyEffect.OpenCurrencies>(it)
        }
    }
}

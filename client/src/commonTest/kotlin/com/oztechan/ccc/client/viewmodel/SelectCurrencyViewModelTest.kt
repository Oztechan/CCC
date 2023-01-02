/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyEffect
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.oztechan.ccc.common.model.Currency as CurrencyCommon

@Suppress("OPT_IN_USAGE")
internal class SelectCurrencyViewModelTest : BaseViewModelTest<SelectCurrencyViewModel>() {

    override val subject: SelectCurrencyViewModel by lazy {
        SelectCurrencyViewModel(currencyDataSource)
    }

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    private val currencyDollar = CurrencyCommon("USD", "Dollar", "$", 0.0, true)
    private val currencyEuro = CurrencyCommon("Eur", "Euro", "", 0.0, true)

    private val currencyUIModel = currencyDollar.toUIModel()

    private val currencyListNotEnough = listOf(currencyDollar)
    private val currencyListEnough = listOf(currencyDollar, currencyEuro)

    @BeforeTest
    override fun setup() {
        super.setup()

        given(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .thenReturn(flowOf(currencyListEnough))
    }

    // SEED
    @Test
    fun check_data_is_null() {
        assertNull(subject.data)
    }

    // init
    @Test
    fun init_updates_the_states_with_no_enough_currency() = runTest {
        given(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .thenReturn(flowOf(currencyListNotEnough))

        subject.state.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.loading }
            assertFalse { it.enoughCurrency }
            assertEquals(currencyListNotEnough.toUIModelList(), it.currencyList)
        }

        verify(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .wasInvoked()
    }

    @Test
    fun init_updates_the_states_with_enough_currency() {
        runTest {
            subject.state.firstOrNull().let {
                assertNotNull(it)
                assertFalse { it.loading }
                assertTrue { it.enoughCurrency }
                assertEquals(currencyListEnough.toUIModelList(), it.currencyList)
            }
        }

        verify(currencyDataSource)
            .invocation { getActiveCurrenciesFlow() }
            .wasInvoked()
    }

    @Test
    fun onItemClick() {
        subject.effect.before {
            subject.event.onItemClick(currencyUIModel)
        }.after {
            assertIs<SelectCurrencyEffect.CurrencyChange>(it)
            assertEquals(currencyUIModel.code, it.newBase)
        }
    }

    @Test
    fun onSelectClick() {
        subject.effect.before {
            subject.event.onSelectClick()
        }.after {
            assertIs<SelectCurrencyEffect.OpenCurrencies>(it)
        }
    }
}

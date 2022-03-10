/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.logmob.initLogger
import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.viewmodel.changebase.ChangeBaseEffect
import com.oztechan.ccc.client.viewmodel.changebase.ChangeBaseState
import com.oztechan.ccc.client.viewmodel.changebase.ChangeBaseViewModel
import com.oztechan.ccc.client.viewmodel.changebase.update
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import com.github.mustafaozhan.ccc.common.model.Currency as CurrencyCommon

class ChangeBaseViewModelTest {

    @Mock
    private val currencyRepository = mock(classOf<CurrencyRepository>())

    private val viewModel: ChangeBaseViewModel by lazy {
        ChangeBaseViewModel(currencyRepository)
    }
    private val currencyDollar = CurrencyCommon("USD", "Dollar", "$", 0.0, true)
    private val currencyEuro = CurrencyCommon("Eur", "Euro", "", 0.0, true)

    private val currencyUIModel = currencyDollar.toUIModel()

    private val currencyListNotEnough = listOf(currencyDollar)
    private val currencyListEnough = listOf(currencyDollar, currencyEuro)

    @BeforeTest
    fun setup() {
        initLogger(true)

        given(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(currencyListEnough))
    }

    // SEED
    @Test
    fun check_data_is_null() {
        assertNull(viewModel.data)
    }

    @Test
    fun states_updates_correctly() {
        val currencyList = listOf(currencyUIModel)
        val state = MutableStateFlow(ChangeBaseState())

        state.before {
            state.update(
                loading = true,
                enoughCurrency = false,
                currencyList = currencyList
            )
        }.after {
            assertEquals(true, it?.loading)
            assertEquals(false, it?.enoughCurrency)
            assertEquals(currencyList, it?.currencyList)
        }
    }

    // init
    @Test
    fun init_updates_the_states_with_no_enough_currency() = runTest {
        given(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flowOf(currencyListNotEnough))

        viewModel.state.firstOrNull().let {
            assertEquals(false, it?.loading)
            assertEquals(false, it?.enoughCurrency)
            assertEquals(currencyListNotEnough.toUIModelList(), it?.currencyList)
        }

        verify(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .wasInvoked()
    }

    @Test
    fun init_updates_the_states_with_enough_currency() {
        runTest {
            viewModel.state.firstOrNull().let {
                assertEquals(false, it?.loading)
                assertEquals(true, it?.enoughCurrency)
                assertEquals(currencyListEnough.toUIModelList(), it?.currencyList)
            }
        }

        verify(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .wasInvoked()
    }

    @Test
    fun onItemClick() {
        viewModel.effect.before {
            viewModel.event.onItemClick(currencyUIModel)
        }.after {
            assertEquals(ChangeBaseEffect.BaseChange(currencyUIModel.name), it)
        }
    }

    @Test
    fun onSelectClick() {
        viewModel.effect.before {
            viewModel.event.onSelectClick()
        }.after {
            assertEquals(ChangeBaseEffect.OpenCurrencies, it)
        }
    }
}

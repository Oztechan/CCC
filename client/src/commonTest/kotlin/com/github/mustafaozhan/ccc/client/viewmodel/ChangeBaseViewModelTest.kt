/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.viewmodel.changebase.ChangeBaseEffect
import com.github.mustafaozhan.ccc.client.viewmodel.changebase.ChangeBaseViewModel
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import kotlinx.coroutines.flow.flow
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ChangeBaseViewModelTest {

    @Mock
    private val currencyRepository = mock(classOf<CurrencyRepository>())

    private val viewModel: ChangeBaseViewModel by lazy {
        ChangeBaseViewModel(currencyRepository)
    }

    private val currency = Currency("USD", "Dollar", "$", 0.0, true)

    @BeforeTest
    fun setup() {
        initLogger(true)

        given(currencyRepository)
            .invocation { collectActiveCurrencies() }
            .thenReturn(flow { listOf(currency) })
    }

    @Test
    fun onItemClick() = with(viewModel) {
        effect.before {
            event.onItemClick(currency)
        }.after {
            assertEquals(ChangeBaseEffect.BaseChange(currency.name), it)
        }
    }

    @Test
    fun onSelectClick() = viewModel.effect.before {
        viewModel.event.onSelectClick()
    }.after {
        assertEquals(ChangeBaseEffect.OpenCurrencies, it)
    }
}

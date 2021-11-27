/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.viewmodel.changebase.ChangeBaseEffect
import com.github.mustafaozhan.ccc.client.viewmodel.changebase.ChangeBaseViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals

class ChangeBaseViewModelTest : BaseViewModelTest<ChangeBaseViewModel>() {

    override val viewModel: ChangeBaseViewModel by lazy {
        koin.getDependency(ChangeBaseViewModel::class)
    }

    @Test
    fun onItemClick() = with(viewModel) {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
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

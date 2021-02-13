/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.BillingPeriod
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first

class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val viewModel: AdRemoveViewModel by lazy {
        koin.getDependency(AdRemoveViewModel::class)
    }

    @Test
    fun setLoading() {
        viewModel.showLoadingView(true)
        assertTrue { viewModel.state.value.loading }
        viewModel.showLoadingView(false)
        assertFalse { viewModel.state.value.loading }
    }

    // Event
    @Test
    fun onWatchVideoClick() = runTest {
        viewModel.event.onWatchVideoClick()
        assertEquals(AdRemoveEffect.WatchVideo, viewModel.effect.first())
    }

    @Test
    fun onBillingClick() = runTest {
        viewModel.event.onBillingClick(BillingPeriod.MONTH)
        assertEquals(AdRemoveEffect.Billing(BillingPeriod.MONTH), viewModel.effect.first())

        viewModel.event.onBillingClick(BillingPeriod.QUARTER)
        assertEquals(AdRemoveEffect.Billing(BillingPeriod.QUARTER), viewModel.effect.first())

        viewModel.event.onBillingClick(BillingPeriod.HALF_YEAR)
        assertEquals(AdRemoveEffect.Billing(BillingPeriod.HALF_YEAR), viewModel.effect.first())

        viewModel.event.onBillingClick(BillingPeriod.YEAR)
        assertEquals(AdRemoveEffect.Billing(BillingPeriod.YEAR), viewModel.effect.first())
    }
}

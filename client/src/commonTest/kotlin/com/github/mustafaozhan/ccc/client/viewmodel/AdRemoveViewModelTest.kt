/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        viewModel.event.onAdRemoveItemClick(RemoveAdType.MONTH)
        assertEquals(AdRemoveEffect.Billing(RemoveAdType.MONTH), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.QUARTER)
        assertEquals(AdRemoveEffect.Billing(RemoveAdType.QUARTER), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.HALF_YEAR)
        assertEquals(AdRemoveEffect.Billing(RemoveAdType.HALF_YEAR), viewModel.effect.first())

        viewModel.event.onAdRemoveItemClick(RemoveAdType.YEAR)
        assertEquals(AdRemoveEffect.Billing(RemoveAdType.YEAR), viewModel.effect.first())
    }
}

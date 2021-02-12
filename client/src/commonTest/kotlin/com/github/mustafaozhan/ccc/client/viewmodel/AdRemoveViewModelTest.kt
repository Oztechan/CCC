/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single

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
}

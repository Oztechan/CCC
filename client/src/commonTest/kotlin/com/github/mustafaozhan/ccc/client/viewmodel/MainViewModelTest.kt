/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals

class MainViewModelTest : BaseViewModelTest<MainViewModel>() {

    override val viewModel: MainViewModel by lazy {
        koin.getDependency(MainViewModel::class)
    }

    @Test
    fun onPause() {
        viewModel.event.onPause().run {
            assertEquals(false, viewModel.data.adVisibility)
            assertEquals(true, viewModel.data.adJob.isCancelled)
        }
    }

    @Test
    fun onResume() {
        viewModel.event.onResume().run {
            assertEquals(true, viewModel.data.adVisibility)
            assertEquals(true, viewModel.data.adJob.isActive)
        }
    }
}

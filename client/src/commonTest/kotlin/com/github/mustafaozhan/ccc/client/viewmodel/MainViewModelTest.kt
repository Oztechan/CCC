/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.model.Device
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import kotlin.test.Test
import kotlin.test.assertEquals

class MainViewModelTest : BaseViewModelTest<MainViewModel>() {

    override val viewModel: MainViewModel by lazy {
        koin.getDependency(MainViewModel::class)
    }

    @Test
    fun onPause() = with(viewModel) {
        event.onPause()
        assertEquals(false, data.adVisibility)
        assertEquals(true, data.adJob.isCancelled)
    }

    @Test
    fun onResume() = with(viewModel) {
        event.onResume()
        if (device is Device.ANDROID.GOOGLE) {
            assertEquals(true, data.adVisibility)
            assertEquals(true, data.adJob.isActive)
        }
    }
}

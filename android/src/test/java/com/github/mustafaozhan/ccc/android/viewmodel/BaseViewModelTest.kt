/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mustafaozhan.ccc.android.rule.TestCoroutineRule
import org.junit.Rule

abstract class BaseViewModelTest<ViewModelType> {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    protected abstract var viewModel: ViewModelType
}

package com.oztechan.ccc.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@Suppress("OPT_IN_USAGE")
abstract class BaseViewModelTest<T> : BaseSubjectTest<T>() {
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    override fun setup() {
        super.setup()
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

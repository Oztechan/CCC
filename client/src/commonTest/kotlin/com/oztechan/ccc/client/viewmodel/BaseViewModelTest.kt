package com.oztechan.ccc.client.viewmodel

import com.github.submob.logmob.initLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@Suppress("OPT_IN_USAGE")
open class BaseViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun start() {
        Dispatchers.setMain(dispatcher)
        initLogger(true)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

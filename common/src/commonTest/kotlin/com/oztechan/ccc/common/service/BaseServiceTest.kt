package com.oztechan.ccc.common.service

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
import kotlin.test.BeforeTest

abstract class BaseServiceTest<T> {
    abstract val service: T

    protected val mockEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    protected val mockThrowable = Throwable("mock")
    protected val mockBase = "EUR"

    @BeforeTest
    fun setup() {
        initLogger(true)
    }
}

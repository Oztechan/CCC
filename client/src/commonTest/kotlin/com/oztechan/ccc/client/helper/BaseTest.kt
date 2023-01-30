package com.oztechan.ccc.client.helper

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import kotlin.test.BeforeTest

open class BaseTest {
    @BeforeTest
    open fun setup() {
        Logger.setLogWriters(CommonWriter()).also {
            Logger.i { "BaseTest setup" }
        }
    }
}

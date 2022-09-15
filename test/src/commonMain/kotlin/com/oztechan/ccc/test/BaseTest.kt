package com.oztechan.ccc.test

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initLogger
import kotlin.test.BeforeTest

open class BaseTest {
    @BeforeTest
    fun setup() {
        initLogger(true).also {
            Logger.i { "BaseTest setup" }
        }
    }
}

package com.oztechan.ccc.test

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initTestLogger
import kotlin.test.BeforeTest

open class BaseTest {
    @BeforeTest
    open fun setup() {
        initTestLogger().also {
            Logger.i { "BaseTest setup" }
        }
    }
}

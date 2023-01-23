package com.oztechan.ccc.client.helper

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

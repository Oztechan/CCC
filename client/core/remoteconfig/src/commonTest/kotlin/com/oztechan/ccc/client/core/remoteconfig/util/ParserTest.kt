package com.oztechan.ccc.client.core.remoteconfig.util

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

internal class ParserTest {

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @Test
    fun `parseToObject returns null when invoked with null`() {
        assertNull(null.parseToObject())
    }

    @Test
    fun `parseToObject returns null when invoked with empty String`() {
        assertNull("".parseToObject())
    }
}

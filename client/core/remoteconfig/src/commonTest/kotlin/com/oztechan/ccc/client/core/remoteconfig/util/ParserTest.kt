package com.oztechan.ccc.client.core.remoteconfig.util

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.remoteconfig.error.NonParsableStringException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class ParserTest {

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @Test
    fun `parseToObject returns null when invoked with null`() {
        assertFailsWith<NonParsableStringException> {
            null.parseToObject()
        }
    }

    @Test
    fun `parseToObject returns null when invoked with empty String`() {
        assertFailsWith<NonParsableStringException> {
            "".parseToObject()
        }
    }
}

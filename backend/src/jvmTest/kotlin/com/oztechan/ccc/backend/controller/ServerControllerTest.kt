package com.oztechan.ccc.backend.controller

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import com.oztechan.ccc.test.BaseSubjectTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("OPT_IN_USAGE")
internal class ServerControllerTest : BaseSubjectTest<ServerController>() {
    override val subject: ServerController by lazy {
        ServerControllerImpl(conversionDataSource)
    }

    @Mock
    private val conversionDataSource = mock(classOf<ConversionDataSource>())

    @Test
    fun `getExchangeRateTextByBase returns getExchangeRateTextByBase from ConversionDataSource`() =
        runTest {
            val base = "EUR"
            val result = "result"

            given(conversionDataSource)
                .coroutine { getExchangeRateTextByBase(base) }
                .thenReturn(result)

            assertEquals(result, subject.getExchangeRateTextByBase(base))

            verify(conversionDataSource)
                .coroutine { getExchangeRateTextByBase(base) }
                .wasInvoked()
        }
}

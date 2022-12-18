package com.oztechan.ccc.backend.controller

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import com.oztechan.ccc.common.datasource.rates.RatesDataSource
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
        ServerControllerImpl(ratesDataSource)
    }

    @Mock
    private val ratesDataSource = mock(classOf<RatesDataSource>())

    @Test
    fun `getOfflineCurrencyResponseByBase returns getCurrencyResponseTextByBase from RatesDataSource`() =
        runTest {
            val base = "EUR"
            val result = "result"

            given(ratesDataSource)
                .coroutine { getCurrencyResponseTextByBase(base) }
                .thenReturn(result)

            assertEquals(result, subject.getCurrencyResponseTextByBase(base))

            verify(ratesDataSource)
                .coroutine { getCurrencyResponseTextByBase(base) }
                .wasInvoked()
        }
}

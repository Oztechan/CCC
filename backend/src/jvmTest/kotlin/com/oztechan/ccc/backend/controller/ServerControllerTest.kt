package com.oztechan.ccc.backend.controller

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import com.oztechan.ccc.common.datasource.offlinerates.OfflineRatesDataSource
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
        ServerControllerImpl(offlineRatesDataSource)
    }

    @Mock
    private val offlineRatesDataSource = mock(classOf<OfflineRatesDataSource>())

    @Test
    fun `getOfflineCurrencyResponseByBase returns getOfflineCurrencyResponseByBase from offlineRatesDataSource`() =
        runTest {
            val base = "EUR"
            val result = "result"

            given(offlineRatesDataSource)
                .coroutine { getOfflineCurrencyResponseByBase(base) }
                .thenReturn(result)

            assertEquals(result, subject.getOfflineCurrencyResponseByBase(base))

            verify(offlineRatesDataSource)
                .coroutine { getOfflineCurrencyResponseByBase(base) }
                .wasInvoked()
        }
}

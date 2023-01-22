package com.oztechan.ccc.backend.controller

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import com.oztechan.ccc.common.core.network.model.Conversion
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import com.oztechan.ccc.common.datasource.exchangerate.ExchangeRateDataSource
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
        ServerControllerImpl(exchangeRateDataSource)
    }

    @Mock
    private val exchangeRateDataSource = mock(classOf<ExchangeRateDataSource>())

    @Test
    fun `getExchangeRateByBase returns getExchangeRateByBase from ExchangeRateDataSource`() =
        runTest {
            val base = "EUR"
            val result = ExchangeRate(base, "date", Conversion(base))

            given(exchangeRateDataSource)
                .coroutine { getExchangeRateByBase(base) }
                .thenReturn(result)

            assertEquals(result, subject.getExchangeRateByBase(base))

            verify(exchangeRateDataSource)
                .coroutine { getExchangeRateByBase(base) }
                .wasInvoked()
        }
}

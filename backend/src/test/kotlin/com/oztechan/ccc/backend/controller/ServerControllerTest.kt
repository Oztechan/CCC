package com.oztechan.ccc.backend.controller

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import com.oztechan.ccc.backend.mapper.toExchangeRateAPIModel
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("OPT_IN_USAGE")
internal class ServerControllerTest {
    private val subject: ServerController by lazy {
        ServerControllerImpl(conversionDataSource)
    }

    @Mock
    private val conversionDataSource = mock(classOf<ConversionDataSource>())

    @Test
    fun `getExchangeRateByBase returns getConversionByBase from ConversionDataSource`() =
        runTest {
            val base = "EUR"
            val result = Conversion(base)

            given(conversionDataSource)
                .coroutine { getConversionByBase(base) }
                .thenReturn(result)

            assertEquals(result.toExchangeRateAPIModel(), subject.getExchangeRateByBase(base))

            verify(conversionDataSource)
                .coroutine { getConversionByBase(base) }
                .wasInvoked()
        }

    @Test
    fun `base is converted to uppercase`() =
        runTest {
            val base = "eur"
            val result = Conversion(base.uppercase())

            given(conversionDataSource)
                .coroutine { getConversionByBase(base.uppercase()) }
                .thenReturn(result)

            assertEquals(result.toExchangeRateAPIModel(), subject.getExchangeRateByBase(base))

            verify(conversionDataSource)
                .coroutine { getConversionByBase(base.uppercase()) }
                .wasInvoked()
        }
}

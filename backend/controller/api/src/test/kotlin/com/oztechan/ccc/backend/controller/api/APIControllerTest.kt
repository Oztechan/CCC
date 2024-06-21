package com.oztechan.ccc.backend.controller.api

import com.oztechan.ccc.backend.controller.api.mapper.toExchangeRateAPIModel
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class APIControllerTest {
    private val subject: APIController by lazy {
        APIControllerImpl(conversionDataSource)
    }

    private val conversionDataSource = mock<ConversionDataSource>()

    @Test
    fun `getExchangeRateByBase returns getConversionByBase from ConversionDataSource`() =
        runTest {
            val base = "EUR"
            val result = Conversion(base)

            everySuspend { conversionDataSource.getConversionByBase(base) }
                .returns(result)

            assertEquals(result.toExchangeRateAPIModel(), subject.getExchangeRateByBase(base))

            verifySuspend { conversionDataSource.getConversionByBase(base) }
        }

    @Test
    fun `base is converted to uppercase`() =
        runTest {
            val base = "eur"
            val result = Conversion(base.uppercase())

            everySuspend { conversionDataSource.getConversionByBase(base.uppercase()) }
                .returns(result)

            assertEquals(result.toExchangeRateAPIModel(), subject.getExchangeRateByBase(base))

            verifySuspend { conversionDataSource.getConversionByBase(base.uppercase()) }
        }
}

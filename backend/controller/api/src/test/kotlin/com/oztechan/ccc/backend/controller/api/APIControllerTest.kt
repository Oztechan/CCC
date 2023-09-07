package com.oztechan.ccc.backend.controller.api

import com.oztechan.ccc.backend.controller.api.mapper.toExchangeRateAPIModel
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class APIControllerTest {
    private val subject: APIController by lazy {
        APIControllerImpl(conversionDataSource)
    }

    @Mock
    private val conversionDataSource = mock(classOf<ConversionDataSource>())

    @Test
    fun `getExchangeRateByBase returns getConversionByBase from ConversionDataSource`() =
        runTest {
            val base = "EUR"
            val result = Conversion(base)

            coEvery { conversionDataSource.getConversionByBase(base) }
                .returns(result)

            assertEquals(result.toExchangeRateAPIModel(), subject.getExchangeRateByBase(base))

            coVerify { conversionDataSource.getConversionByBase(base) }
                .wasInvoked()
        }

    @Test
    fun `base is converted to uppercase`() =
        runTest {
            val base = "eur"
            val result = Conversion(base.uppercase())

            coEvery { conversionDataSource.getConversionByBase(base.uppercase()) }
                .returns(result)

            assertEquals(result.toExchangeRateAPIModel(), subject.getExchangeRateByBase(base))

            coVerify { conversionDataSource.getConversionByBase(base.uppercase()) }
                .wasInvoked()
        }
}

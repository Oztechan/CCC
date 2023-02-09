/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.service.premium

import com.oztechan.ccc.common.core.network.api.premium.PremiumApi
import com.oztechan.ccc.common.core.network.mapper.toConversionModel
import com.oztechan.ccc.common.core.network.model.Conversion
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
internal class PremiumApiServiceTest {

    private val subject: PremiumApiService by lazy {
        PremiumApiServiceImpl(premiumAPI, newSingleThreadContext(this::class.simpleName.toString()))
    }

    @Mock
    private val premiumAPI: PremiumApi = mock(classOf())

    private val base = "EUR"
    private val conversion = Conversion(base)
    private val exchangeRate = ExchangeRate(base, "12.21.2121", conversion)
    private val throwable = Throwable("mock")

    @Test
    fun `getConversion parameter can not be empty`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
        }

        verify(premiumAPI)
            .coroutine { premiumAPI.getExchangeRate("") }
            .wasInvoked()
    }

    @Test
    fun `getConversion error`() = runTest {
        given(premiumAPI)
            .coroutine { premiumAPI.getExchangeRate(base) }
            .thenThrow(throwable)

        runCatching { subject.getConversion(base) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(throwable.message, it.exceptionOrNull()!!.cause!!.message)
        }

        verify(premiumAPI)
            .coroutine { getExchangeRate(base) }
            .wasInvoked()
    }

    @Test
    fun `getConversion success`() = runTest {
        given(premiumAPI)
            .coroutine { premiumAPI.getExchangeRate(base) }
            .thenReturn(exchangeRate)

        runCatching { subject.getConversion(base) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(conversion.toConversionModel(), it.getOrNull())
        }

        verify(premiumAPI)
            .coroutine { getExchangeRate(base) }
            .wasInvoked()
    }
}

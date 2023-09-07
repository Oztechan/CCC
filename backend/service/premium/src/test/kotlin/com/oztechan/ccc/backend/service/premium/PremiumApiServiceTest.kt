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
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.mock
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class PremiumApiServiceTest {

    private val subject: PremiumApiService by lazy {
        @Suppress("OPT_IN_USAGE")
        PremiumApiServiceImpl(premiumAPI, UnconfinedTestDispatcher())
    }

    @Mock
    private val premiumAPI: PremiumApi = mock(classOf())

    private val base = "EUR"
    private val exchangeRate = ExchangeRate(base, "12.21.2121", Conversion(base))
    private val throwable = Throwable("mock")

    @Test
    fun `getConversion parameter can not be empty API call is not made`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
        }

        coVerify { premiumAPI.getExchangeRate("") }
            .wasNotInvoked()
    }

    @Test
    fun `getConversion error`() = runTest {
        coEvery { premiumAPI.getExchangeRate(base) }
            .throws(throwable)

        runCatching { subject.getConversion(base) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(throwable.message, it.exceptionOrNull()!!.cause!!.message)
        }

        coVerify { premiumAPI.getExchangeRate(base) }
            .wasInvoked()
    }

    @Test
    fun `getConversion success`() = runTest {
        coEvery { premiumAPI.getExchangeRate(base) }
            .returns(exchangeRate)

        runCatching { subject.getConversion(base) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertNotNull(it.getOrNull())
            assertEquals(exchangeRate.toConversionModel(), it.getOrNull())
        }

        coVerify { premiumAPI.getExchangeRate(base) }
            .wasInvoked()
    }
}

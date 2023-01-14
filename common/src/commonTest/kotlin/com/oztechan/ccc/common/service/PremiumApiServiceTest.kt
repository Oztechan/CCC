/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.model.Conversion
import com.oztechan.ccc.common.api.model.ExchangeRate
import com.oztechan.ccc.common.api.premium.PremiumApi
import com.oztechan.ccc.common.error.UnknownNetworkException
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.service.premium.PremiumApiServiceImpl
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
internal class PremiumApiServiceTest : BaseSubjectTest<PremiumApiService>() {

    override val subject: PremiumApiService by lazy {
        PremiumApiServiceImpl(premiumAPI, createTestDispatcher())
    }

    @Mock
    private val premiumAPI = mock(classOf<PremiumApi>())

    private val mockEntity = ExchangeRate("EUR", "12.21.2121", Conversion())
    private val mockThrowable = Throwable("mock")
    private val mockBase = "EUR"

    @Test
    fun `getConversion parameter can not be empty`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(premiumAPI)
            .coroutine { premiumAPI.getConversion("") }
            .wasInvoked()
    }

    @Test
    fun `getConversion error`() = runTest {
        given(premiumAPI)
            .coroutine { premiumAPI.getConversion(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { subject.getConversion(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(mockThrowable.message, it.exceptionOrNull()!!.cause!!.message)
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(premiumAPI)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()
    }

    @Test
    fun `getConversion success`() = runTest {
        given(premiumAPI)
            .coroutine { premiumAPI.getConversion(mockBase) }
            .thenReturn(mockEntity)

        runCatching { subject.getConversion(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(premiumAPI)
            .coroutine { getConversion(mockBase) }
            .wasInvoked()
    }
}

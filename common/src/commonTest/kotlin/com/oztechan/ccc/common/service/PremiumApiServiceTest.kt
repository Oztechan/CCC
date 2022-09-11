/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.Rates
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
class PremiumApiServiceTest : BaseSubjectTest<PremiumApiService>() {

    override val subject: PremiumApiService by lazy {
        PremiumApiServiceImpl(premiumAPI, createTestDispatcher())
    }

    @Mock
    private val premiumAPI = mock(classOf<PremiumApi>())

    private val mockEntity = CurrencyResponse("EUR", "12.21.2121", Rates())
    private val mockThrowable = Throwable("mock")
    private val mockBase = "EUR"

    @Test
    fun getRates_parameter_can_not_be_empty() = runTest {
        runCatching { subject.getRates("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(premiumAPI)
            .coroutine { premiumAPI.getRates("") }
            .wasInvoked()
    }

    @Test
    fun getRates_error() = runTest {
        given(premiumAPI)
            .coroutine { premiumAPI.getRates(mockBase) }
            .thenThrow(mockThrowable)

        runCatching { subject.getRates(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull()?.message)
            assertEquals(mockThrowable.message, it.exceptionOrNull()!!.message)
            assertIs<UnknownNetworkException>(it.exceptionOrNull())
        }

        verify(premiumAPI)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }

    @Test
    fun getRates_success() = runTest {
        given(premiumAPI)
            .coroutine { premiumAPI.getRates(mockBase) }
            .thenReturn(mockEntity)

        runCatching { subject.getRates(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(premiumAPI)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }
}

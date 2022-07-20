/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.service

import com.oztechan.ccc.common.api.premium.PremiumApi
import com.oztechan.ccc.common.error.UnknownNetworkException
import com.oztechan.ccc.common.mapper.toModel
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.common.service.premium.PremiumApiServiceImpl
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
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
class PremiumApiServiceTest : BaseServiceTest<PremiumApiService>() {

    @Mock
    private val premiumAPI = mock(classOf<PremiumApi>())

    override val service: PremiumApiService = PremiumApiServiceImpl(
        premiumAPI,
        newSingleThreadContext(this::class.simpleName.toString())
    )

    @Test
    fun getRates_parameter_can_not_be_empty() = runTest {
        runCatching { service.getRates("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertTrue { it.exceptionOrNull() is UnknownNetworkException }
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

        runCatching { service.getRates(mockBase) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertEquals(mockThrowable.message, it.exceptionOrNull()?.message)
            assertTrue { it.exceptionOrNull() is UnknownNetworkException }
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

        runCatching { service.getRates(mockBase) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertEquals(mockEntity.toModel(), it.getOrNull())
        }

        verify(premiumAPI)
            .coroutine { getRates(mockBase) }
            .wasInvoked()
    }
}

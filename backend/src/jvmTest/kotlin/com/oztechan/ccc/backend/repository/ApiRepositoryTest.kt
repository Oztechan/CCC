@file:Suppress("OPT_IN_USAGE")

package com.oztechan.ccc.backend.repository

import com.oztechan.ccc.backend.repository.api.ApiRepository
import com.oztechan.ccc.backend.repository.api.ApiRepositoryImpl
import com.oztechan.ccc.common.datasource.rates.RatesDataSource
import com.oztechan.ccc.common.service.free.FreeApiService
import com.oztechan.ccc.common.service.premium.PremiumApiService
import com.oztechan.ccc.test.BaseSubjectTest
import com.oztechan.ccc.test.util.createTestDispatcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ApiRepositoryTest : BaseSubjectTest<ApiRepository>() {
    override val subject: ApiRepository by lazy {
        ApiRepositoryImpl(
            premiumApiService,
            freeApiService,
            ratesDataSource,
            TestScope(),
            createTestDispatcher()
        )
    }

    @Mock
    private val premiumApiService = mock(classOf<PremiumApiService>())

    @Mock
    private val freeApiService = mock(classOf<FreeApiService>())

    @Mock
    private val ratesDataSource = mock(classOf<RatesDataSource>())

    @Test
    fun `getOfflineCurrencyResponseByBase returns getCurrencyResponseTextByBase from RatesDataSource`() =
        runTest {
            val base = "EUR"
            val result = "result"

            given(ratesDataSource)
                .coroutine { getCurrencyResponseTextByBase(base) }
                .thenReturn(result)

            assertEquals(result, subject.getCurrencyResponseTextByBase(base))

            verify(ratesDataSource)
                .coroutine { getCurrencyResponseTextByBase(base) }
                .wasInvoked()
        }
}

package com.oztechan.ccc.client.service.backend

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.network.api.backend.BackendApi
import com.oztechan.ccc.common.core.network.mapper.toConversionModel
import com.oztechan.ccc.common.core.network.model.Conversion
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class BackendApiServiceTest {

    private val subject: BackendApiService by lazy {
        @Suppress("OPT_IN_USAGE")
        BackendApiServiceImpl(backendApi, UnconfinedTestDispatcher())
    }

    private val backendApi = mock<BackendApi>()

    private val base = "EUR"
    private val exchangeRate = ExchangeRate(base, "12.21.2121", Conversion(base))
    private val throwable = Throwable("mock")

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @Test
    fun `getConversion parameter can not be empty API call is not made`() = runTest {
        runCatching { subject.getConversion("") }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
        }

        verifySuspend(VerifyMode.not) { backendApi.getExchangeRate("") }
    }

    @Test
    fun `getConversion error`() = runTest {
        everySuspend { backendApi.getExchangeRate(base) }
            .throws(throwable)

        runCatching { subject.getConversion(base) }.let {
            assertFalse { it.isSuccess }
            assertTrue { it.isFailure }
            assertNotNull(it.exceptionOrNull())
            assertNotNull(it.exceptionOrNull()!!.cause)
            assertNotNull(it.exceptionOrNull()!!.message)
            assertEquals(throwable.message, it.exceptionOrNull()!!.cause!!.message)
        }

        verifySuspend { backendApi.getExchangeRate(base) }
    }

    @Test
    fun `getConversion success`() = runTest {
        everySuspend { backendApi.getExchangeRate(base) }
            .returns(exchangeRate)

        runCatching { subject.getConversion(base) }.let {
            assertTrue { it.isSuccess }
            assertFalse { it.isFailure }
            assertNotNull(it.getOrNull())
            assertEquals(exchangeRate.toConversionModel(), it.getOrNull())
        }

        verifySuspend { backendApi.getExchangeRate(base) }
    }
}

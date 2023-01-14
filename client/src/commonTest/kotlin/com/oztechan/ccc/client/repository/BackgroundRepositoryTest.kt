package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.repository.background.BackgroundRepository
import com.oztechan.ccc.client.repository.background.BackgroundRepositoryImpl
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.ExchangeRate
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.test.BaseSubjectTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
internal class BackgroundRepositoryTest : BaseSubjectTest<BackgroundRepository>() {

    override val subject: BackgroundRepository by lazy {
        BackgroundRepositoryImpl(watcherDataSource, backendApiService)
    }

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    @Test
    fun `if getWatchers throw an error should return false`() = runTest {
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenThrow(Exception())

        assertFalse { subject.shouldSendNotification() }

        verify(watcherDataSource)
            .coroutine { getWatchers() }
            .wasInvoked()
    }

    @Test
    fun `if getConversion throw an error should return false`() = runTest {
        val watcher = Watcher(1, "EUR", "USD", true, 1.1)

        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher))

        given(backendApiService)
            .coroutine { getConversion(watcher.base) }
            .thenThrow(Exception())

        assertFalse { subject.shouldSendNotification() }

        verify(watcherDataSource)
            .coroutine { getWatchers() }
            .wasInvoked()

        verify(backendApiService)
            .coroutine { getConversion(watcher.base) }
            .wasInvoked()
    }

    @Test
    fun `if watcher set greater and response rate is more than watcher return true`() = runTest {
        val watcher = Watcher(1, "EUR", "USD", true, 1.1)

        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher))

        given(backendApiService)
            .coroutine { getConversion(watcher.base) }
            .thenReturn(ExchangeRate(watcher.base, "", Conversion(base = watcher.base, usd = watcher.rate + 1)))

        assertTrue { subject.shouldSendNotification() }

        verify(watcherDataSource)
            .coroutine { getWatchers() }
            .wasInvoked()

        verify(backendApiService)
            .coroutine { getConversion(watcher.base) }
            .wasInvoked()
    }

    @Test
    fun `if watcher set not greater and response rate is less than watcher return true`() = runTest {
        val watcher = Watcher(1, "EUR", "USD", false, 1.1)

        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher))

        given(backendApiService)
            .coroutine { getConversion(watcher.base) }
            .thenReturn(ExchangeRate(watcher.base, "", Conversion(base = watcher.base, usd = watcher.rate - 1)))

        assertTrue { subject.shouldSendNotification() }

        verify(watcherDataSource)
            .coroutine { getWatchers() }
            .wasInvoked()

        verify(backendApiService)
            .coroutine { getConversion(watcher.base) }
            .wasInvoked()
    }
}

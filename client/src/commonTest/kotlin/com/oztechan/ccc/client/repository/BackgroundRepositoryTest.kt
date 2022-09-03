package com.oztechan.ccc.client.repository

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.client.repository.background.BackgroundRepository
import com.oztechan.ccc.client.repository.background.BackgroundRepositoryImpl
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.common.service.backend.BackendApiService
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse

class BackgroundRepositoryTest {
    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val backendApiService = mock(classOf<BackendApiService>())

    private val repository: BackgroundRepository by lazy {
        BackgroundRepositoryImpl(watcherDataSource, backendApiService)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)
    }

    @Test
    fun `if getWatchers throw an error no notification should be send`() = runTest {
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenThrow(Exception())

        assertFalse { repository.shouldSendNotification() }

        verify(watcherDataSource)
            .coroutine { getWatchers() }
            .wasInvoked()
    }

    @Test
    fun `if getRates throw an error no notification should be send`() = runTest {
        val watcher = Watcher(1, "EUR", "USD", true, 1.1)

        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher))

        given(backendApiService)
            .coroutine { getRates(watcher.base) }
            .thenThrow(Exception())

        assertFalse { repository.shouldSendNotification() }

        verify(watcherDataSource)
            .coroutine { getWatchers() }
            .wasInvoked()

        verify(backendApiService)
            .coroutine { getRates(watcher.base) }
            .wasInvoked()
    }
}

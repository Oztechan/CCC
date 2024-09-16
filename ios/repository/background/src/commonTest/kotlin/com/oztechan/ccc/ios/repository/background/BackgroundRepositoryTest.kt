package com.oztechan.ccc.ios.repository.background

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Watcher
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BackgroundRepositoryTest {

    private val subject: BackgroundRepository by lazy {
        BackgroundRepositoryImpl(watcherDataSource, backendApiService)
    }

    private val watcherDataSource = mock<WatcherDataSource>()

    private val backendApiService = mock<BackendApiService>()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @Test
    fun `if getWatchers throw an error should return false`() = runTest {
        everySuspend { watcherDataSource.getWatchers() }
            .throws(Exception())

        assertFalse { subject.shouldSendNotification() }

        verifySuspend { watcherDataSource.getWatchers() }
    }

    @Test
    fun `if getConversion throw an error should return false`() = runTest {
        val watcher = Watcher(1, "EUR", "USD", true, 1.1)

        everySuspend { watcherDataSource.getWatchers() }
            .returns(listOf(watcher))

        everySuspend { backendApiService.getConversion(watcher.source) }
            .throws(Exception())

        assertFalse { subject.shouldSendNotification() }

        verifySuspend { watcherDataSource.getWatchers() }

        verifySuspend { backendApiService.getConversion(watcher.source) }
    }

    @Test
    fun `if watcher set greater and response rate is more than watcher return true`() = runTest {
        val watcher = Watcher(1, "EUR", "USD", true, 1.1)

        everySuspend { watcherDataSource.getWatchers() }
            .returns(listOf(watcher))

        everySuspend { backendApiService.getConversion(watcher.source) }
            .returns(Conversion(base = watcher.source, usd = watcher.rate + 1))

        assertTrue { subject.shouldSendNotification() }

        verifySuspend { watcherDataSource.getWatchers() }

        verifySuspend { backendApiService.getConversion(watcher.source) }
    }

    @Test
    fun `if watcher set not greater and response rate is less than watcher return true`() =
        runTest {
            val watcher = Watcher(1, "EUR", "USD", false, 1.1)

            everySuspend { watcherDataSource.getWatchers() }
                .returns(listOf(watcher))

            everySuspend { backendApiService.getConversion(watcher.source) }
                .returns(Conversion(base = watcher.source, usd = watcher.rate - 1))

            assertTrue { subject.shouldSendNotification() }

            verifySuspend { watcherDataSource.getWatchers() }

            verifySuspend { backendApiService.getConversion(watcher.source) }
        }
}

package com.oztechan.ccc.client.viewmodel.watchers

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.util.toStandardDigits
import com.oztechan.ccc.client.core.shared.util.toSupportedCharacters
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.Watcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

internal class WatchersViewModelTest {

    private val viewModel: WatchersViewModel by lazy {
        WatchersViewModel(
            currencyDataSource,
            watcherDataSource,
            adControlRepository,
            analyticsManager
        )
    }

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val watcherDataSource =
        configure(mock(classOf<WatcherDataSource>())) { stubsUnitByDefault = true }

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    @Mock
    private val analyticsManager =
        configure(mock(classOf<AnalyticsManager>())) { stubsUnitByDefault = true }

    private val watcher = Watcher(1, "EUR", "USD", true, 1.1)

    private val watcherList = listOf(watcher, watcher)
    private val shouldShowAds = Random.nextBoolean()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { watcherDataSource.getWatchersFlow() }
            .returns(flowOf(watcherList))

        runTest {
            coEvery { adControlRepository.shouldShowBannerAd() }
                .returns(shouldShowAds)
        }
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(shouldShowAds, it.isBannerAdVisible)
            assertEquals(watcherList, it.watcherList)
        }

        coVerify { adControlRepository.shouldShowBannerAd() }
            .wasInvoked()
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        viewModel // init

        verify {
            analyticsManager.setUserProperty(
                UserProperty.WatcherCount(
                    watcherList.count().toString()
                )
            )
        }
            .wasInvoked()
    }

    // Event
    @Test
    fun onBackClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onBackClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WatchersEffect.Back>(it)
        }
    }

    @Test
    fun onBaseClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onBaseClick(watcher)
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WatchersEffect.SelectBase>(it)
            assertEquals(watcher, it.watcher)
        }
    }

    @Test
    fun onTargetClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onTargetClick(watcher)
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WatchersEffect.SelectTarget>(it)
            assertEquals(watcher, it.watcher)
        }
    }

    @Test
    fun onBaseChanged() {
        val mockBase = "mock"
        viewModel.event.onBaseChanged(watcher, mockBase)

        runTest {
            coVerify { watcherDataSource.updateWatcherBaseById(mockBase, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onTargetChanged() {
        val mockBase = "mock"
        viewModel.event.onTargetChanged(watcher, mockBase)

        runTest {
            coVerify { watcherDataSource.updateWatcherTargetById(mockBase, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onAddClick() = runTest {
        val currency1 = Currency("USD", "Dollar", "", "", true)
        val currency2 = Currency("EUR", "EUR", "", "", true)

        // when there is no active currency
        coEvery { currencyDataSource.getActiveCurrencies() }
            .returns(listOf())

        coEvery { watcherDataSource.getWatchers() }
            .returns(listOf())

        viewModel.event.onAddClick()

        coVerify { watcherDataSource.addWatcher("", "") }
            .wasInvoked()

        // when there is few watcher
        coEvery { watcherDataSource.getWatchers() }
            .returns(listOf(watcher))

        coEvery { currencyDataSource.getActiveCurrencies() }
            .returns(listOf(currency1, currency2))

        viewModel.event.onAddClick()

        coVerify { watcherDataSource.addWatcher(currency1.code, currency2.code) }
            .wasInvoked()

        // when there are so much watcher
        coEvery { watcherDataSource.getWatchers() }
            .returns(listOf(watcher, watcher, watcher, watcher, watcher))

        viewModel.effect.onSubscription {
            viewModel.event.onAddClick()
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WatchersEffect.MaximumNumberOfWatchers>(it)
        }
    }

    @Test
    fun onDeleteClick() {
        viewModel.event.onDeleteClick(watcher)

        runTest {
            coVerify { watcherDataSource.deleteWatcher(watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRelationChange() {
        val mockBoolean = Random.nextBoolean()
        viewModel.event.onRelationChange(watcher, mockBoolean)

        runTest {
            coVerify { watcherDataSource.updateWatcherRelationById(mockBoolean, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRateChange() = runTest {
        // when rate is normal
        var rate = "12"
        assertEquals(rate, viewModel.event.onRateChange(watcher, rate))

        coVerify {
            watcherDataSource.updateWatcherRateById(
                rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull() ?: 0.0,
                watcher.id
            )
        }
            .wasInvoked()

        // when rate is not valid
        viewModel.effect.onSubscription {
            rate = "asd"
            assertEquals(rate, viewModel.event.onRateChange(watcher, rate))
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WatchersEffect.InvalidInput>(it)
        }

        // when rate is too long
        viewModel.effect.onSubscription {
            rate = "12345678910"
            assertEquals(rate.dropLast(1), viewModel.event.onRateChange(watcher, rate))
        }.firstOrNull().let {
            assertNotNull(it)
            assertIs<WatchersEffect.TooBigInput>(it)
        }
    }
}

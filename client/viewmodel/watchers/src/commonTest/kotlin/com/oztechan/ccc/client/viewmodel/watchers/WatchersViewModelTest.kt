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
import io.mockative.configure
import io.mockative.given
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

        given(watcherDataSource)
            .invocation { getWatchersFlow() }
            .thenReturn(flowOf(watcherList))

        given(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(shouldShowAds)
    }

    // init
    @Test
    fun `init updates states correctly`() = runTest {
        viewModel.state.firstOrNull().let {
            assertNotNull(it)
            assertEquals(shouldShowAds, it.isBannerAdVisible)
            assertEquals(watcherList, it.watcherList)
        }

        verify(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    // Analytics
    @Test
    fun ifUserPropertiesSetCorrect() {
        viewModel // init

        verify(analyticsManager)
            .invocation {
                setUserProperty(
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
            verify(watcherDataSource)
                .coroutine { updateWatcherBaseById(mockBase, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onTargetChanged() {
        val mockBase = "mock"
        viewModel.event.onTargetChanged(watcher, mockBase)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateWatcherTargetById(mockBase, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onAddClick() = runTest {
        val currency1 = Currency("USD", "Dollar", "", "", true)
        val currency2 = Currency("EUR", "EUR", "", "", true)

        // when there is no active currency
        given(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .thenReturn(listOf())

        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf())

        viewModel.event.onAddClick()

        verify(watcherDataSource)
            .coroutine { addWatcher("", "") }
            .wasInvoked()

        // when there is few watcher
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher))

        given(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .thenReturn(listOf(currency1, currency2))

        viewModel.event.onAddClick()

        verify(watcherDataSource)
            .coroutine { addWatcher(currency1.code, currency2.code) }
            .wasInvoked()

        // when there are so much watcher
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher, watcher, watcher, watcher, watcher))

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
            verify(watcherDataSource)
                .coroutine { deleteWatcher(watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRelationChange() {
        val mockBoolean = Random.nextBoolean()
        viewModel.event.onRelationChange(watcher, mockBoolean)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateWatcherRelationById(mockBoolean, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRateChange() = runTest {
        // when rate is normal
        var rate = "12"
        assertEquals(rate, viewModel.event.onRateChange(watcher, rate))

        verify(watcherDataSource)
            .coroutine {
                updateWatcherRateById(
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

package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.client.helper.BaseViewModelTest
import com.oztechan.ccc.client.helper.util.after
import com.oztechan.ccc.client.helper.util.before
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.util.toSupportedCharacters
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEffect
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.Watcher
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@Suppress("OPT_IN_USAGE", "TooManyFunctions")
internal class WatchersViewModelTest : BaseViewModelTest<WatchersViewModel>() {

    override val subject: WatchersViewModel by lazy {
        WatchersViewModel(currencyDataSource, watcherDataSource, adControlRepository)
    }

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val adControlRepository = mock(classOf<AdControlRepository>())

    private val watcher = Watcher(1, "EUR", "USD", true, 1.1)

    @BeforeTest
    override fun setup() {
        super.setup()

        given(watcherDataSource)
            .invocation { getWatchersFlow() }
            .thenReturn(flowOf(listOf(watcher)))
    }

    @Test
    fun shouldShowBannerAd() {
        val mockBool = Random.nextBoolean()

        given(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBool)

        assertEquals(mockBool, subject.shouldShowBannerAd())

        verify(adControlRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    // Event
    @Test
    fun onBackClick() = subject.effect.before {
        subject.event.onBackClick()
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.Back>(it)
    }

    @Test
    fun onBaseClick() = subject.effect.before {
        subject.event.onBaseClick(watcher)
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.SelectBase>(it)
        assertEquals(watcher, it.watcher)
    }

    @Test
    fun onTargetClick() = subject.effect.before {
        subject.event.onTargetClick(watcher)
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.SelectTarget>(it)
        assertEquals(watcher, it.watcher)
    }

    @Test
    fun onBaseChanged() {
        val mockBase = "mock"
        subject.event.onBaseChanged(watcher, mockBase)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateWatcherBaseById(mockBase, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onTargetChanged() {
        val mockBase = "mock"
        subject.event.onTargetChanged(watcher, mockBase)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateWatcherTargetById(mockBase, watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onAddClick() = runTest {
        val currency1 = Currency("USD", "Dollar", "", 1.2, true)
        val currency2 = Currency("EUR", "EUR", "", 1.2, true)

        // when there is no active currency
        given(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .thenReturn(listOf())

        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf())

        subject.event.onAddClick()

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

        subject.event.onAddClick()

        verify(watcherDataSource)
            .coroutine { addWatcher(currency1.code, currency2.code) }
            .wasInvoked()

        // when there are so much watcher
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher, watcher, watcher, watcher, watcher))

        subject.effect.before {
            subject.event.onAddClick()
        }.after {
            assertNotNull(it)
            assertIs<WatchersEffect.MaximumNumberOfWatchers>(it)
        }
    }

    @Test
    fun onDeleteClick() {
        subject.event.onDeleteClick(watcher)

        runTest {
            verify(watcherDataSource)
                .coroutine { deleteWatcher(watcher.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRelationChange() {
        val mockBoolean = Random.nextBoolean()
        subject.event.onRelationChange(watcher, mockBoolean)

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
        assertEquals(rate, subject.event.onRateChange(watcher, rate))

        verify(watcherDataSource)
            .coroutine {
                updateWatcherRateById(
                    rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull() ?: 0.0,
                    watcher.id
                )
            }
            .wasInvoked()

        // when rate is not valid
        rate = "asd"
        subject.effect.before {
            assertEquals(rate, subject.event.onRateChange(watcher, rate))
        }.after {
            assertNotNull(it)
            assertIs<WatchersEffect.InvalidInput>(it)
        }
        // when rate is too long
        rate = "12345678910"
        subject.effect.before {
            assertEquals(rate.dropLast(1), subject.event.onRateChange(watcher, rate))
        }.after {
            assertNotNull(it)
            assertIs<WatchersEffect.TooBigNumber>(it)
        }
    }
}

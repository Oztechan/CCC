package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.util.toSupportedCharacters
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEffect
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
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
        WatchersViewModel(currencyDataSource, watcherDataSource, adRepository)
    }

    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    private val watcher = Watcher(1, "EUR", "USD", true, 1.1)
    private val watcherUIModel = watcher.toUIModel()

    @BeforeTest
    override fun setup() {
        super.setup()

        given(watcherDataSource)
            .invocation { collectWatchers() }
            .thenReturn(flowOf(listOf(watcher)))
    }

    @Test
    fun shouldShowBannerAd() {
        val mockBool = Random.nextBoolean()

        given(adRepository)
            .invocation { shouldShowBannerAd() }
            .thenReturn(mockBool)

        assertEquals(mockBool, subject.shouldShowBannerAd())

        verify(adRepository)
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
        subject.event.onBaseClick(watcherUIModel)
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.SelectBase>(it)
        assertEquals(watcherUIModel, it.watcher)
    }

    @Test
    fun onTargetClick() = subject.effect.before {
        subject.event.onTargetClick(watcherUIModel)
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.SelectTarget>(it)
        assertEquals(watcherUIModel, it.watcher)
    }

    @Test
    fun onBaseChanged() {
        val mockBase = "mock"
        subject.event.onBaseChanged(watcherUIModel, mockBase)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateBaseById(mockBase, watcherUIModel.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onTargetChanged() {
        val mockBase = "mock"
        subject.event.onTargetChanged(watcherUIModel, mockBase)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateTargetById(mockBase, watcherUIModel.id) }
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
            .coroutine { addWatcher(currency1.name, currency2.name) }
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
        subject.event.onDeleteClick(watcherUIModel)

        runTest {
            verify(watcherDataSource)
                .coroutine { deleteWatcher(watcherUIModel.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRelationChange() {
        val mockBoolean = Random.nextBoolean()
        subject.event.onRelationChange(watcherUIModel, mockBoolean)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateRelationById(mockBoolean, watcherUIModel.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRateChange() = runTest {
        // when rate is normal
        var rate = "12"
        assertEquals(rate, subject.event.onRateChange(watcherUIModel, rate))

        verify(watcherDataSource)
            .coroutine {
                updateRateById(
                    rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull() ?: 0.0,
                    watcherUIModel.id
                )
            }
            .wasInvoked()

        // when rate is not valid
        rate = "asd"
        subject.effect.before {
            assertEquals(rate, subject.event.onRateChange(watcherUIModel, rate))
        }.after {
            assertNotNull(it)
            assertIs<WatchersEffect.InvalidInput>(it)
        }
        // when rate is too long
        rate = "12345678910"
        subject.effect.before {
            assertEquals(rate.dropLast(1), subject.event.onRateChange(watcherUIModel, rate))
        }.after {
            assertNotNull(it)
            assertIs<WatchersEffect.TooBigNumber>(it)
        }
    }
}

package com.oztechan.ccc.client.viewmodel

import com.github.submob.logmob.initLogger
import com.oztechan.ccc.client.mapper.toUIModel
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEffect
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.datasource.watcher.WatcherDataSource
import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.model.Watcher
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

class WatchersViewModelTest : BaseViewModelTest() {
    @Mock
    private val currencyDataSource = mock(classOf<CurrencyDataSource>())

    @Mock
    private val watcherDataSource = mock(classOf<WatcherDataSource>())

    @Mock
    private val adRepository = mock(classOf<AdRepository>())

    private val viewModel: WatchersViewModel by lazy {
        WatchersViewModel(currencyDataSource, watcherDataSource, adRepository)
    }

    private val watcher = Watcher(1, "EUR", "USD", true, 1.1)
    private val watcherUIModel = watcher.toUIModel()

    @BeforeTest
    fun setup() {
        initLogger(true)

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

        assertEquals(mockBool, viewModel.shouldShowBannerAd())

        verify(adRepository)
            .invocation { shouldShowBannerAd() }
            .wasInvoked()
    }

    // Event
    @Test
    fun onBackClick() = viewModel.effect.before {
        viewModel.event.onBackClick()
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.Back>(it)
    }

    @Test
    fun onBaseClick() = viewModel.effect.before {
        viewModel.event.onBaseClick(watcherUIModel)
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.SelectBase>(it)
        assertEquals(watcherUIModel, it.watcher)
    }

    @Test
    fun onTargetClick() = viewModel.effect.before {
        viewModel.event.onTargetClick(watcherUIModel)
    }.after {
        assertNotNull(it)
        assertIs<WatchersEffect.SelectTarget>(it)
        assertEquals(watcherUIModel, it.watcher)
    }

    @Test
    fun onBaseChanged() {
        val mockBase = "mock"
        viewModel.event.onBaseChanged(watcherUIModel, mockBase)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateBaseById(mockBase, watcherUIModel.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onTargetChanged() {
        val mockBase = "mock"
        viewModel.event.onTargetChanged(watcherUIModel, mockBase)

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

        // when there is few watcher
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher))

        given(currencyDataSource)
            .coroutine { getActiveCurrencies() }
            .thenReturn(listOf(currency1, currency2))

        viewModel.event.onAddClick()

        verify(watcherDataSource)
            .coroutine { addWatcher(currency1.name, currency2.name) }
            .wasInvoked()

        // when there are so much watcher
        given(watcherDataSource)
            .coroutine { getWatchers() }
            .thenReturn(listOf(watcher, watcher, watcher, watcher, watcher))

        viewModel.effect.before {
            viewModel.event.onAddClick()
        }.after {
            assertNotNull(it)
            assertIs<WatchersEffect.MaximumNumberOfWatchers>(it)
        }
    }

    @Test
    fun onDeleteClick() {
        viewModel.event.onDeleteClick(watcherUIModel)

        runTest {
            verify(watcherDataSource)
                .coroutine { deleteWatcher(watcherUIModel.id) }
                .wasInvoked()
        }
    }

    @Test
    fun onRelationChange() {
        val mockBoolean = Random.nextBoolean()
        viewModel.event.onRelationChange(watcherUIModel, mockBoolean)

        runTest {
            verify(watcherDataSource)
                .coroutine { updateRelationById(mockBoolean, watcherUIModel.id) }
                .wasInvoked()
        }
    }
}

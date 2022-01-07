/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.mapper.toUIModel
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesState
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.update
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.RemoteConfig
import com.github.mustafaozhan.config.model.AdConfig
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.logmob.initLogger
import io.mockative.ConfigurationApi
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.github.mustafaozhan.ccc.client.model.Currency as ClientCurrency
import com.github.mustafaozhan.ccc.common.model.Currency as CommonCurrency

@ConfigurationApi
class CurrenciesViewModelTest {

    @Mock
    private val settingsRepository = configure(mock(classOf<SettingsRepository>())) {
        stubsUnitByDefault = true
    }

    @Mock
    private val currencyRepository = configure(mock(classOf<CurrencyRepository>())) {
        stubsUnitByDefault = true
    }

    @Mock
    private val remoteConfig = mock(classOf<RemoteConfig>())

    private val viewModel: CurrenciesViewModel by lazy {
        CurrenciesViewModel(settingsRepository, currencyRepository, remoteConfig)
    }

    private val commonCurrency = CommonCurrency("EUR", "Euro", "€", isActive = true)
    private val clientCurrency = commonCurrency.toUIModel()

    private val currencyListCommon = listOf(commonCurrency)
    private val currencyListClient = listOf(clientCurrency)

    private val currencyListFlow = flowOf(currencyListCommon)

    @BeforeTest
    fun setup() {
        initLogger(true)

        given(currencyRepository)
            .invocation { collectAllCurrencies() }
            .thenReturn(currencyListFlow)
    }

    // SEED
    @Test
    fun states_updates_correctly() {
        val state = MutableStateFlow(CurrenciesState())

        state.before {
            state.update(
                currencyList = currencyListClient,
                loading = true,
                selectionVisibility = true
            )
        }.after {
            assertEquals(currencyListClient, it?.currencyList)
            assertEquals(true, it?.loading)
            assertEquals(true, it?.selectionVisibility)
        }
    }

    // init
    @Test
    fun init_updates_states_correctly() = runTest {
        viewModel.state.firstOrNull().let {
            assertEquals(currencyListClient, it?.currencyList)
            assertEquals(false, it?.selectionVisibility)
            assertEquals(currencyListClient.toMutableList(), viewModel.data.unFilteredList)
        }
    }

    // public methods
    @Test
    fun hideSelectionVisibility() = viewModel.state.before {
        viewModel.hideSelectionVisibility()
    }.after {
        assertEquals(false, it?.selectionVisibility)
    }

    @Test
    fun shouldShowBannerAd() {
        val mockLong = Random.nextLong() + nowAsLong()
        val mockBoolean = Random.nextBoolean()
        val mockAppConfig = AppConfig(AdConfig(isBannerAdEnabled = mockBoolean))
        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockLong)

        given(remoteConfig)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(
            mockLong.isRewardExpired() && mockAppConfig.adConfig.isBannerAdEnabled,
            viewModel.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(remoteConfig)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun isFirstRun() {
        val mockValue = Random.nextBoolean()
        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(mockValue)

        assertEquals(mockValue, viewModel.isFirstRun())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun queryGetUpdatedOnFilteringList() {
        val query = "query"
        // runTest can be removed after kotlin move to new memory management
        runTest {
            viewModel.event.onQueryChange(query)
            assertEquals(query, viewModel.data.query)
        }
    }

    // Event
    @Test
    fun updateAllCurrenciesState() {
        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(false)

        given(settingsRepository)
            .invocation { currentBase }
            .thenReturn("EUR")

        val mockValue = Random.nextBoolean()
        viewModel.event.updateAllCurrenciesState(mockValue)

        verify(currencyRepository)
            .invocation { updateAllCurrencyState(mockValue) }
            .wasInvoked()
    }

    @Test
    fun onItemClick() {
        viewModel.event.onItemClick(clientCurrency)

        verify(currencyRepository)
            .invocation {
                updateCurrencyStateByName(
                    clientCurrency.name,
                    !clientCurrency.isActive
                )
            }.wasInvoked()
    }

    @Test
    fun onQueryChange() = with(viewModel) {
        val dollar = ClientCurrency("USD", "American Dollar", "$")

        val originalList = mutableListOf<ClientCurrency>().apply {
            add(clientCurrency)
            add(dollar)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("USD")
        }.after {
            assertEquals(true, it?.currencyList?.contains(dollar))
            assertEquals("USD", viewModel.data.query)
            assertEquals(false, it?.loading)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("Euro")
        }.after {
            assertEquals(true, it?.currencyList?.contains(clientCurrency))
            assertEquals("Euro", viewModel.data.query)
            assertEquals(false, it?.loading)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("$")
        }.after {
            assertEquals(true, it?.currencyList?.contains(dollar))
            assertEquals("$", viewModel.data.query)
            assertEquals(false, it?.loading)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("something")
        }.after {
            assertEquals(true, it?.currencyList?.isEmpty())
            assertEquals("something", viewModel.data.query)
            assertEquals(false, it?.loading)
        }

        viewModel.state.before {
            data.unFilteredList = originalList
            event.onQueryChange("o")
        }.after {
            assertEquals(2, it?.currencyList?.size)
            assertEquals("o", viewModel.data.query)
            assertEquals(false, it?.loading)
        }
    }

    @Test
    fun onItemLongClick() {
        val previousValue = viewModel.state.value.selectionVisibility
        viewModel.state.before {
            viewModel.event.onItemLongClick()
        }.after {
            assertEquals(!previousValue, it?.selectionVisibility)
        }

        viewModel.state.before {
            viewModel.event.onItemLongClick()
        }.after {
            assertEquals(previousValue, it?.selectionVisibility)
        }
    }

    @Test
    fun onCloseClick() {
        viewModel.effect.before {
            viewModel.onCloseClick()
        }.after {
            assertTrue { it is CurrenciesEffect.Back }
            assertEquals("", viewModel.data.query)
        }

        // show selections
        viewModel.event.onItemLongClick()

        viewModel.state.before {
            viewModel.onCloseClick()
        }.after {
            assertEquals(false, it?.selectionVisibility)
            assertEquals("", viewModel.data.query)
        }
    }

    @Test
    fun onDoneClick() {
        val dollar = ClientCurrency("USD", "American Dollar", "$", isActive = true)

        viewModel.effect.before {
            viewModel.onDoneClick()
        }.after {
            assertTrue { it is CurrenciesEffect.FewCurrency }
        }

        // given
        viewModel.data.unFilteredList = mutableListOf(clientCurrency, dollar)

        viewModel.effect.before {
            viewModel.onDoneClick()
        }.after {
            assertTrue { it is CurrenciesEffect.OpenCalculator }

            verify(settingsRepository)
                .invocation { firstRun = false }
                .wasInvoked()
        }
    }
}

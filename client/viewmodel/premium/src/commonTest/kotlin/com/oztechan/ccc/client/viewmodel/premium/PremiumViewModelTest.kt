/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.premium

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.model.OldPurchase
import com.oztechan.ccc.client.core.shared.model.PremiumData
import com.oztechan.ccc.client.core.shared.model.PremiumType
import com.oztechan.ccc.client.core.shared.util.calculatePremiumEnd
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.storage.app.AppStorage
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@Suppress("OPT_IN_USAGE")
internal class PremiumViewModelTest {

    private val viewModel: PremiumViewModel by lazy {
        PremiumViewModel(appStorage)
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    // SEED
    @Test
    fun `check data is null`() {
        assertNull(viewModel.data)
    }

    // public methods
    @Test
    fun updatePremiumEndDate() = runTest {
        viewModel.updatePremiumEndDate(null)
        verify(appStorage)
            .invocation { premiumEndDate }
            .wasNotInvoked()

        PremiumType.values().forEach { premiumType ->
            viewModel.effect.onSubscription {
                viewModel.updatePremiumEndDate(premiumType)
            }.firstOrNull().let {
                assertIs<PremiumEffect.PremiumActivated>(it)
                assertEquals(premiumType, it.premiumType)
                assertFalse { it.isRestorePurchase }

                verify(appStorage)
                    .invocation { premiumEndDate = premiumType.calculatePremiumEnd() }
                    .wasInvoked()
            }
        }
    }

    @Test
    fun restorePurchase() = runTest {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(0)

        viewModel.effect.onSubscription {
            viewModel.restorePurchase(
                listOf(
                    OldPurchase(nowAsLong(), PremiumType.MONTH),
                    OldPurchase(nowAsLong(), PremiumType.YEAR)
                )
            )
        }.firstOrNull().let {
            assertIs<PremiumEffect.PremiumActivated>(it)
            assertTrue { it.isRestorePurchase }

            verify(appStorage)
                .invocation { premiumEndDate = it.premiumType.calculatePremiumEnd(nowAsLong()) }
                .wasInvoked()
        }
    }

    @Test
    fun `restorePurchase should fail if all the old purchases out dated`() {
        val oldPurchase = OldPurchase(nowAsLong(), PremiumType.MONTH)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + 1.seconds.inWholeMilliseconds)

        viewModel.restorePurchase(listOf(oldPurchase))

        verify(appStorage)
            .invocation { premiumEndDate = oldPurchase.type.calculatePremiumEnd(oldPurchase.date) }
            .wasNotInvoked()
    }

    @Test
    fun `restorePurchase should fail if all the old purchases expired`() {
        val oldPurchase = OldPurchase(nowAsLong() - (32.days.inWholeMilliseconds), PremiumType.MONTH)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(0)

        viewModel.restorePurchase(listOf(oldPurchase))

        verify(appStorage)
            .invocation { premiumEndDate = oldPurchase.type.calculatePremiumEnd(oldPurchase.date) }
            .wasNotInvoked()
    }

    @Test
    fun showLoadingView() {
        val mockValue = Random.nextBoolean()

        viewModel.showLoadingView(mockValue)

        assertEquals(mockValue, viewModel.state.value.loading)
    }

    @Test
    fun addPurchaseMethods() = runTest {
        PremiumType.values()
            .map { it.data }
            .forEach { premiumData ->
                viewModel.state.onSubscription {
                    viewModel.addPurchaseMethods(listOf(premiumData))
                }.firstOrNull().let {
                    assertNotNull(it)
                    assertTrue { it.premiumTypes.contains(PremiumType.getById(premiumData.id)) }
                    assertFalse { it.loading }
                }
            }
    }

    @Test
    fun `addPurchaseMethods for unknown id will not add the item`() = runTest {
        viewModel.state.onSubscription {
            viewModel.addPurchaseMethods(listOf(PremiumData("", "", "unknown")))
        }.firstOrNull().let {
            assertNotNull(it)
            println(it.premiumTypes.toString())
            assertTrue { it.premiumTypes.isNotEmpty() } // only video should be there
            assertEquals(PremiumType.VIDEO, it.premiumTypes.first())
            assertFalse { it.loading }
        }
    }

    // Event
    @Test
    fun onPremiumItemClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.VIDEO)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.VIDEO, it.premiumType)
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.MONTH)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.MONTH, it.premiumType)
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.QUARTER)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.QUARTER, it.premiumType)
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.HALF_YEAR)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.HALF_YEAR, it.premiumType)
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.YEAR)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.YEAR, it.premiumType)
        }
    }
}

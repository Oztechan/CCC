/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel.premium

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.viewmodel.premium.model.OldPurchase
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumData
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType
import com.oztechan.ccc.client.viewmodel.premium.util.calculatePremiumEnd
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

internal class PremiumViewModelTest {

    private val viewModel: PremiumViewModel by lazy {
        PremiumViewModel(appStorage)
    }

    @Mock
    private val appStorage = configure(mock(classOf<AppStorage>())) { stubsUnitByDefault = true }

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())

        @Suppress("OPT_IN_USAGE")
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    // SEED
    @Test
    fun `check data is null`() {
        assertNull(viewModel.data)
    }

    // Event
    @Test
    fun onPremiumActivated() = runTest {
        viewModel.event.onPremiumActivated(null)
        coVerify { appStorage.getPremiumEndDate() }
            .wasNotInvoked()

        PremiumType.values().forEach { premiumType ->
            val now = nowAsLong()
            viewModel.effect.onSubscription {
                viewModel.event.onPremiumActivated(premiumType, now)
            }.firstOrNull().let {
                assertIs<PremiumEffect.PremiumActivated>(it)
                assertEquals(premiumType, it.premiumType)
                assertFalse { it.isRestorePurchase }

                coVerify { appStorage.setPremiumEndDate(premiumType.calculatePremiumEnd(now)) }
                    .wasInvoked()
            }
        }
    }

    @Test
    fun onRestorePurchase() = runTest {
        coEvery { appStorage.getPremiumEndDate() }
            .returns(0)

        val now = nowAsLong()

        viewModel.effect.onSubscription {
            viewModel.event.onRestorePurchase(
                listOf(
                    OldPurchase(now, PremiumType.MONTH),
                    OldPurchase(now, PremiumType.YEAR)
                )
            )
        }.firstOrNull().let {
            assertIs<PremiumEffect.PremiumActivated>(it)
            assertTrue { it.isRestorePurchase }
            assertFalse { viewModel.state.value.loading }

            coVerify { appStorage.setPremiumEndDate(it.premiumType.calculatePremiumEnd(now)) }
                .wasInvoked()
        }

        // onRestorePurchase shouldn't do anything if all the old purchases out of dated
        var oldPurchase = OldPurchase(nowAsLong(), PremiumType.MONTH)

        coEvery { appStorage.getPremiumEndDate() }
            .returns(nowAsLong() + 1.seconds.inWholeMilliseconds)

        viewModel.event.onRestorePurchase(listOf(oldPurchase))

        coVerify { appStorage.setPremiumEndDate(oldPurchase.type.calculatePremiumEnd(oldPurchase.date)) }
            .wasNotInvoked()

        // onRestorePurchase shouldn't do anything if the old purchase is already expired
        oldPurchase = OldPurchase(nowAsLong() - (32.days.inWholeMilliseconds), PremiumType.MONTH)

        coEvery { appStorage.getPremiumEndDate() }
            .returns(0)

        viewModel.event.onRestorePurchase(listOf(oldPurchase))

        coVerify { appStorage.setPremiumEndDate(oldPurchase.type.calculatePremiumEnd(oldPurchase.date)) }
            .wasNotInvoked()
    }

    @Test
    fun onAddPurchaseMethods() = runTest {
        // in case called an empty list loading should be false
        viewModel.state.onSubscription {
            viewModel.event.onAddPurchaseMethods(emptyList())
        }.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.loading }
        }

        PremiumType.values()
            .map { it.data }
            .forEach { premiumData ->
                viewModel.state.onSubscription {
                    viewModel.event.onAddPurchaseMethods(listOf(premiumData))
                }.firstOrNull().let {
                    assertNotNull(it)
                    assertTrue { it.premiumTypes.contains(PremiumType.getById(premiumData.id)) }
                    assertFalse { it.loading }
                }
            }

        // in case called an unknown id item should not be added
        viewModel.state.onSubscription {
            viewModel.event.onAddPurchaseMethods(listOf(PremiumData("", "", "unknown")))
        }.firstOrNull().let {
            assertNotNull(it)
            println(it.premiumTypes.toString())
            assertTrue { it.premiumTypes.isNotEmpty() } // only video should be there
            assertEquals(PremiumType.VIDEO, it.premiumTypes.first())
            assertFalse { it.loading }
        }
    }

    @Test
    fun onPremiumItemClick() = runTest {
        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.VIDEO)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.VIDEO, it.premiumType)
            assertFalse { viewModel.state.value.loading }
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.MONTH)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.MONTH, it.premiumType)
            assertTrue { viewModel.state.value.loading }
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.QUARTER)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.QUARTER, it.premiumType)
            assertTrue { viewModel.state.value.loading }
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.HALF_YEAR)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.HALF_YEAR, it.premiumType)
            assertTrue { viewModel.state.value.loading }
        }

        viewModel.effect.onSubscription {
            viewModel.event.onPremiumItemClick(PremiumType.YEAR)
        }.firstOrNull().let {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.YEAR, it.premiumType)
            assertTrue { viewModel.state.value.loading }
        }
    }

    @Test
    fun onPremiumActivationFailed() = runTest {
        viewModel.state.onSubscription {
            viewModel.onPremiumActivationFailed()
        }.firstOrNull().let {
            assertNotNull(it)
            assertFalse { it.loading }
        }
    }
}

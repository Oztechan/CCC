/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.PremiumData
import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.util.calculatePremiumEnd
import com.oztechan.ccc.client.viewmodel.premium.PremiumEffect
import com.oztechan.ccc.client.viewmodel.premium.PremiumViewModel
import com.oztechan.ccc.common.core.infrastructure.util.DAY
import com.oztechan.ccc.common.core.infrastructure.util.SECOND
import com.oztechan.ccc.common.core.infrastructure.util.nowAsLong
import com.oztechan.ccc.test.BaseViewModelTest
import com.oztechan.ccc.test.util.after
import com.oztechan.ccc.test.util.before
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("OPT_IN_USAGE")
internal class PremiumViewModelTest : BaseViewModelTest<PremiumViewModel>() {

    override val subject: PremiumViewModel by lazy {
        PremiumViewModel(appStorage)
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    // SEED
    @Test
    fun `check data is null`() {
        assertNull(subject.data)
    }

    // public methods
    @Test
    fun updatePremiumEndDate() {
        subject.updatePremiumEndDate(null)
        verify(appStorage)
            .invocation { premiumEndDate }
            .wasNotInvoked()

        PremiumType.values().forEach { premiumType ->
            subject.effect.before {
                subject.updatePremiumEndDate(premiumType)
            }.after {
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
    fun restorePurchase() {
        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(0)

        subject.effect.before {
            subject.restorePurchase(
                listOf(
                    OldPurchase(nowAsLong(), PremiumType.MONTH),
                    OldPurchase(nowAsLong(), PremiumType.YEAR)
                )
            )
        }.after {
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
            .thenReturn(nowAsLong() + SECOND)

        subject.restorePurchase(listOf(oldPurchase))

        verify(appStorage)
            .invocation { premiumEndDate = oldPurchase.type.calculatePremiumEnd(oldPurchase.date) }
            .wasNotInvoked()
    }

    @Test
    fun `restorePurchase should fail if all the old purchases expired`() {
        val oldPurchase = OldPurchase(nowAsLong() - (DAY * 32), PremiumType.MONTH)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(0)

        subject.restorePurchase(listOf(oldPurchase))

        verify(appStorage)
            .invocation { premiumEndDate = oldPurchase.type.calculatePremiumEnd(oldPurchase.date) }
            .wasNotInvoked()
    }

    @Test
    fun showLoadingView() {
        val mockValue = Random.nextBoolean()

        subject.showLoadingView(mockValue)

        assertEquals(mockValue, subject.state.value.loading)
    }

    @Test
    fun addPurchaseMethods() = PremiumType.values()
        .map { it.data }
        .forEach { premiumData ->
            subject.state.before {
                subject.addPurchaseMethods(listOf(premiumData))
            }.after {
                assertNotNull(it)
                assertTrue { it.premiumTypes.contains(PremiumType.getById(premiumData.id)) }
                assertFalse { it.loading }
            }
        }

    @Test
    fun `addPurchaseMethods for unknown id will not add the item`() = subject.state.before {
        subject.addPurchaseMethods(listOf(PremiumData("", "", "unknown")))
    }.after {
        assertNotNull(it)
        println(it.premiumTypes.toString())
        assertTrue { it.premiumTypes.isNotEmpty() } // only video should be there
        assertEquals(PremiumType.VIDEO, it.premiumTypes.first())
        assertFalse { it.loading }
    }

    // Event
    @Test
    fun onPremiumItemClick() = with(subject) {
        effect.before {
            event.onPremiumItemClick(PremiumType.VIDEO)
        }.after {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.VIDEO, it.premiumType)
        }

        effect.before {
            event.onPremiumItemClick(PremiumType.MONTH)
        }.after {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.MONTH, it.premiumType)
        }

        effect.before {
            event.onPremiumItemClick(PremiumType.QUARTER)
        }.after {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.QUARTER, it.premiumType)
        }

        effect.before {
            event.onPremiumItemClick(PremiumType.HALF_YEAR)
        }.after {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.HALF_YEAR, it.premiumType)
        }

        effect.before {
            event.onPremiumItemClick(PremiumType.YEAR)
        }.after {
            assertIs<PremiumEffect.LaunchActivatePremiumFlow>(it)
            assertEquals(PremiumType.YEAR, it.premiumType)
        }
    }
}

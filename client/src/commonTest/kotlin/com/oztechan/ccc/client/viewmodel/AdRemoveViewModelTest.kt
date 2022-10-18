/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.RemoveAdData
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.oztechan.ccc.common.storage.AppStorage
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.common.util.nowAsLong
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
internal class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val subject: AdRemoveViewModel by lazy {
        AdRemoveViewModel(appStorage)
    }

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    // SEED
    @Test
    fun check_data_is_null() {
        assertNull(subject.data)
    }

    // public methods
    @Test
    fun updateAddFreeDate() {
        subject.updateAddFreeDate(null)
        verify(appStorage)
            .invocation { adFreeEndDate }
            .wasNotInvoked()

        RemoveAdType.values().forEach { adRemoveType ->
            subject.effect.before {
                subject.updateAddFreeDate(adRemoveType)
            }.after {
                assertIs<AdRemoveEffect.AdsRemoved>(it)
                assertEquals(adRemoveType, it.removeAdType)
                assertFalse { it.isRestorePurchase }

                verify(appStorage)
                    .invocation { adFreeEndDate = adRemoveType.calculateAdRewardEnd() }
                    .wasInvoked()
            }
        }
    }

    @Test
    fun restorePurchase() {
        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        subject.effect.before {
            subject.restorePurchase(
                listOf(
                    OldPurchase(nowAsLong(), RemoveAdType.MONTH),
                    OldPurchase(nowAsLong(), RemoveAdType.YEAR)
                )
            )
        }.after {
            assertIs<AdRemoveEffect.AdsRemoved>(it)
            assertTrue { it.isRestorePurchase }

            verify(appStorage)
                .invocation { adFreeEndDate = it.removeAdType.calculateAdRewardEnd(nowAsLong()) }
                .wasInvoked()
        }
    }

    @Test
    fun `restorePurchase should fail if all the old purchases out dated`() {
        val oldPurchase = OldPurchase(nowAsLong(), RemoveAdType.MONTH)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() + SECOND)

        subject.restorePurchase(listOf(oldPurchase))

        verify(appStorage)
            .invocation { adFreeEndDate = oldPurchase.type.calculateAdRewardEnd(oldPurchase.date) }
            .wasNotInvoked()
    }

    @Test
    fun `restorePurchase should fail if all the old purchases expired`() {
        val oldPurchase = OldPurchase(nowAsLong() - (DAY * 32), RemoveAdType.MONTH)

        given(appStorage)
            .invocation { adFreeEndDate }
            .thenReturn(0)

        subject.restorePurchase(listOf(oldPurchase))

        verify(appStorage)
            .invocation { adFreeEndDate = oldPurchase.type.calculateAdRewardEnd(oldPurchase.date) }
            .wasNotInvoked()
    }

    @Test
    fun showLoadingView() {
        val mockValue = Random.nextBoolean()

        subject.showLoadingView(mockValue)

        assertEquals(mockValue, subject.state.value.loading)
    }

    @Test
    fun addPurchaseMethods() = RemoveAdType.values()
        .map { it.data }
        .forEach { removeAdData ->
            subject.state.before {
                subject.addPurchaseMethods(listOf(removeAdData))
            }.after {
                assertNotNull(it)
                assertTrue { it.adRemoveTypes.contains(RemoveAdType.getById(removeAdData.id)) }
                assertFalse { it.loading }
            }
        }

    @Test
    fun `addPurchaseMethods for unknown id will not add the item`() = subject.state.before {
        subject.addPurchaseMethods(listOf(RemoveAdData("", "", "unknown")))
    }.after {
        assertNotNull(it)
        println(it.adRemoveTypes.toString())
        assertTrue { it.adRemoveTypes.isNotEmpty() } // only video should be there
        assertEquals(RemoveAdType.VIDEO, it.adRemoveTypes.first())
        assertFalse { it.loading }
    }

    // Event
    @Test
    fun onAdRemoveItemClick() = with(subject) {
        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.VIDEO)
        }.after {
            assertIs<AdRemoveEffect.LaunchRemoveAdFlow>(it)
            assertEquals(RemoveAdType.VIDEO, it.removeAdType)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.MONTH)
        }.after {
            assertIs<AdRemoveEffect.LaunchRemoveAdFlow>(it)
            assertEquals(RemoveAdType.MONTH, it.removeAdType)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.QUARTER)
        }.after {
            assertIs<AdRemoveEffect.LaunchRemoveAdFlow>(it)
            assertEquals(RemoveAdType.QUARTER, it.removeAdType)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.HALF_YEAR)
        }.after {
            assertIs<AdRemoveEffect.LaunchRemoveAdFlow>(it)
            assertEquals(RemoveAdType.HALF_YEAR, it.removeAdType)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.YEAR)
        }.after {
            assertIs<AdRemoveEffect.LaunchRemoveAdFlow>(it)
            assertEquals(RemoveAdType.YEAR, it.removeAdType)
        }
    }
}

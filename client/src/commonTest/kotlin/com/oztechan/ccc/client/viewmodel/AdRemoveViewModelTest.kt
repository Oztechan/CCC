/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.viewmodel

import com.oztechan.ccc.client.model.OldPurchase
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.client.util.after
import com.oztechan.ccc.client.util.before
import com.oztechan.ccc.client.util.calculateAdRewardEnd
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveState
import com.oztechan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.oztechan.ccc.client.viewmodel.adremove.update
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.test.BaseViewModelTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val subject: AdRemoveViewModel by lazy {
        AdRemoveViewModel(settingsDataSource)
    }

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    // SEED
    @Test
    fun check_data_is_null() {
        assertNull(subject.data)
    }

    @Test
    fun states_updates_correctly() {
        val adRemoveTypes = RemoveAdType.values().toList()
        val loading = Random.nextBoolean()
        val state = MutableStateFlow(AdRemoveState())

        state.update(
            adRemoveTypes = adRemoveTypes,
            loading = loading
        )

        state.value.let {
            assertEquals(loading, it.loading)
            assertEquals(adRemoveTypes, it.adRemoveTypes)
        }
    }

    // public methods
    @Test
    fun updateAddFreeDate() {
        subject.updateAddFreeDate(null)
        verify(settingsDataSource)
            .invocation { adFreeEndDate }
            .wasNotInvoked()

        RemoveAdType.values().forEach { adRemoveType ->
            subject.effect.before {
                subject.updateAddFreeDate(adRemoveType)
            }.after {
                assertIs<AdRemoveEffect.AdsRemoved>(it)
                assertEquals(adRemoveType, it.removeAdType)
                assertFalse { it.isRestorePurchase }

                verify(settingsDataSource)
                    .invocation { adFreeEndDate = adRemoveType.calculateAdRewardEnd() }
                    .wasInvoked()
            }
        }
    }

    @Test
    fun restorePurchase() {
        given(settingsDataSource)
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
        }
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
            }
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

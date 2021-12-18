/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.model.OldPurchase
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdRemoveViewModelTest {

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    private val viewModel: AdRemoveViewModel by lazy {
        AdRemoveViewModel(settingsRepository)
    }

    @Test
    fun setLoading() {
        viewModel.state.before {
            viewModel.showLoadingView(true)
        }.after {
            assertEquals(true, it?.loading)
        }
        viewModel.state.before {
            viewModel.showLoadingView(false)
        }.after {
            assertEquals(false, it?.loading)
        }
    }

    @Test
    fun updateAddFreeDate() {
        given(settingsRepository)
            .setter(settingsRepository::adFreeEndDate)
            .whenInvokedWith(any())
            .thenReturn(Unit)

        RemoveAdType.values().forEach { adRemoveType ->
            viewModel.effect.before {
                viewModel.updateAddFreeDate(adRemoveType)
            }.after {
                assertEquals(AdRemoveEffect.AdsRemoved(adRemoveType, false), it)
            }
        }
    }

    @Test
    fun restorePurchase() {
        given(settingsRepository)
            .getter(settingsRepository::adFreeEndDate)
            .whenInvoked()
            .thenReturn(0)

        given(settingsRepository)
            .setter(settingsRepository::adFreeEndDate)
            .whenInvokedWith(any())
            .thenReturn(Unit)

        viewModel.effect.before {
            viewModel.restorePurchase(
                listOf(
                    OldPurchase(nowAsLong(), RemoveAdType.MONTH),
                    OldPurchase(nowAsLong(), RemoveAdType.YEAR)
                )
            )
        }.after {
            assertTrue { it is AdRemoveEffect.AdsRemoved }
            assertEquals(true, (it as? AdRemoveEffect.AdsRemoved)?.isRestorePurchase == true)
        }
    }

    @Test
    fun addPurchaseMethods() = RemoveAdType.values()
        .map { it.data }
        .forEach { removeAdData ->
            viewModel.state.before {
                viewModel.addPurchaseMethods(listOf(removeAdData))
            }.after {
                assertEquals(
                    true,
                    it?.adRemoveTypes?.contains(RemoveAdType.getById(removeAdData.id))
                )
            }
        }

    // Event
    @Test
    fun onBillingClick() = with(viewModel) {
        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.VIDEO)
        }.after {
            assertEquals(AdRemoveEffect.LaunchRemoveAdFlow(RemoveAdType.VIDEO), it)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.MONTH)
        }.after {
            assertEquals(AdRemoveEffect.LaunchRemoveAdFlow(RemoveAdType.MONTH), it)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.QUARTER)
        }.after {
            assertEquals(AdRemoveEffect.LaunchRemoveAdFlow(RemoveAdType.QUARTER), it)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.HALF_YEAR)
        }.after {
            assertEquals(AdRemoveEffect.LaunchRemoveAdFlow(RemoveAdType.HALF_YEAR), it)
        }

        effect.before {
            event.onAdRemoveItemClick(RemoveAdType.YEAR)
        }.after {
            assertEquals(AdRemoveEffect.LaunchRemoveAdFlow(RemoveAdType.YEAR), it)
        }
    }
}

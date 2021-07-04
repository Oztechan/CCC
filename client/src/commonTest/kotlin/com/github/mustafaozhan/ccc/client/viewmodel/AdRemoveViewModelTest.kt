/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val viewModel: AdRemoveViewModel by lazy {
        koin.getDependency(AdRemoveViewModel::class)
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
    fun updateAddFreeDate() = RemoveAdType.values().forEach { adRemoveType ->
        viewModel.effect.before {
            viewModel.updateAddFreeDate(adRemoveType)
        }.after {
            assertEquals(AdRemoveEffect.AdsRemoved(adRemoveType), it)
        }
    }

    @Test
    fun restorePurchase() = viewModel.effect.before {
        viewModel.restorePurchase(
            listOf(
                PurchaseHistory(nowAsLong(), RemoveAdType.MONTH),
                PurchaseHistory(nowAsLong(), RemoveAdType.YEAR)
            )
        )
    }.after {
        assertTrue { it is AdRemoveEffect.AlreadyAdFree || it is AdRemoveEffect.AdsRemoved }
    }

    @Test
    fun addInAppBillingMethods() = RemoveAdType.values()
        .map { it.data }
        .forEach { removeAdData ->
            viewModel.state.before {
                viewModel.addInAppBillingMethods(listOf(removeAdData))
            }.after {
                assertEquals(
                    true,
                    it?.adRemoveTypes?.contains(RemoveAdType.getBySku(removeAdData.skuId))
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

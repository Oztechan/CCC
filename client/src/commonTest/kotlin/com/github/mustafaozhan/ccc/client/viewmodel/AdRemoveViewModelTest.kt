/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.test
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val viewModel: AdRemoveViewModel by lazy {
        koin.getDependency(AdRemoveViewModel::class)
    }

    @Test
    fun setLoading() = runTest {
        viewModel.showLoadingView(true)
        assertEquals(true, viewModel.state.value.loading)

        viewModel.showLoadingView(false)
        assertEquals(false, viewModel.state.value.loading)
    }

    @Test
    fun updateAddFreeDate() = RemoveAdType.values().forEach {
        viewModel.effect.test({
            viewModel.updateAddFreeDate(it)
        }, {
            assertEquals(AdRemoveEffect.RestartActivity, it)
        })
    }

    @Test
    fun restorePurchase() = viewModel.effect.test({
        viewModel.restorePurchase(
            listOf(
                PurchaseHistory(nowAsLong(), RemoveAdType.MONTH),
                PurchaseHistory(nowAsLong(), RemoveAdType.YEAR)
            )
        )
    }, {
        assertTrue { it is AdRemoveEffect.AlreadyAdFree || it is AdRemoveEffect.RestartActivity }
    })

    @Test
    fun addInAppBillingMethods() = runTest {
        RemoveAdType.values()
            .map { it.data }
            .forEach {
                viewModel.addInAppBillingMethods(listOf(it))
                assertEquals(
                    true,
                    viewModel.state.value.adRemoveTypes.contains(RemoveAdType.getBySku(it.skuId))
                )
            }
    }

    // Event
    @Test
    fun onBillingClick() = with(viewModel) {
        effect.test({
            event.onAdRemoveItemClick(RemoveAdType.VIDEO)
        }, {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.VIDEO), it)
        })

        effect.test({
            event.onAdRemoveItemClick(RemoveAdType.MONTH)
        }, {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.MONTH), it)
        })

        effect.test({
            event.onAdRemoveItemClick(RemoveAdType.QUARTER)
        }, {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.QUARTER), it)
        })

        effect.test({
            event.onAdRemoveItemClick(RemoveAdType.HALF_YEAR)
        }, {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.HALF_YEAR), it)
        })

        effect.test({
            event.onAdRemoveItemClick(RemoveAdType.YEAR)
        }, {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.YEAR), it)
        })
    }
}

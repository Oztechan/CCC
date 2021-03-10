/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModelTest
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdRemoveViewModelTest : BaseViewModelTest<AdRemoveViewModel>() {

    override val viewModel: AdRemoveViewModel by lazy {
        koin.getDependency(AdRemoveViewModel::class)
    }

    @Test
    fun setLoading() {
        viewModel.showLoadingView(true).run {
            assertTrue { viewModel.state.value.loading }
        }
        viewModel.showLoadingView(false).run {
            assertFalse { viewModel.state.value.loading }
        }
    }

    @Test
    fun updateAddFreeDate() = runTest {
        RemoveAdType.values().forEach {
            viewModel.updateAddFreeDate(it).run {
                assertEquals(AdRemoveEffect.RestartActivity, viewModel.effect.first())
            }
        }
    }

    @Test
    fun restorePurchase() = runTest {
        viewModel.restorePurchase(
            listOf(
                PurchaseHistory(nowAsLong(), RemoveAdType.MONTH),
                PurchaseHistory(nowAsLong(), RemoveAdType.YEAR)
            )
        ).run {
            viewModel.effect.first().let {
                assertTrue {
                    it is AdRemoveEffect.AlreadyAdFree || it is AdRemoveEffect.RestartActivity
                }
            }
        }
    }

    @Test
    fun addInAppBillingMethods() = runTest {
        RemoveAdType.values()
            .map { it.data }
            .forEach {
                viewModel.addInAppBillingMethods(listOf(it)).run {
                    assertTrue {
                        viewModel.state.value.adRemoveTypes.contains(RemoveAdType.getBySku(it.skuId))
                    }
                }
            }
    }

    // Event
    @Test
    fun onBillingClick() = runTest {
        viewModel.event.onAdRemoveItemClick(RemoveAdType.VIDEO).run {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.VIDEO), viewModel.effect.first())
        }
        viewModel.event.onAdRemoveItemClick(RemoveAdType.MONTH).run {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.MONTH), viewModel.effect.first())
        }
        viewModel.event.onAdRemoveItemClick(RemoveAdType.QUARTER).run {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.QUARTER), viewModel.effect.first())
        }
        viewModel.event.onAdRemoveItemClick(RemoveAdType.HALF_YEAR).run {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.HALF_YEAR), viewModel.effect.first())
        }
        viewModel.event.onAdRemoveItemClick(RemoveAdType.YEAR).run {
            assertEquals(AdRemoveEffect.RemoveAd(RemoveAdType.YEAR), viewModel.effect.first())
        }
    }
}

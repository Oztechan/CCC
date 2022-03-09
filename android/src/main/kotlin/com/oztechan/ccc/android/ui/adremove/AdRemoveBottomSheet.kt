/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.android.ui.adremove

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.android.util.showDialog
import com.oztechan.ccc.android.util.showLoading
import com.oztechan.ccc.android.util.showSnack
import com.oztechan.ccc.android.util.toOldPurchaseList
import com.oztechan.ccc.android.util.toRemoveAdDataList
import com.oztechan.ccc.billing.BillingEffect
import com.oztechan.ccc.billing.BillingManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetAdRemoveBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class AdRemoveBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetAdRemoveBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val billingManager: BillingManager by inject()
    private val adRemoveViewModel: AdRemoveViewModel by viewModel()

    private val removeAdsAdapter: RemoveAdsAdapter by lazy {
        RemoveAdsAdapter(adRemoveViewModel.event)
    }

    override fun getViewBinding() = BottomSheetAdRemoveBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "AdRemoveBottomSheet onViewCreated" }
        billingManager.startConnection(
            viewLifecycleOwner.lifecycleScope,
            RemoveAdType.getPurchaseIds()
        )
        initViews()
        observeStates()
        observeEffects()
        observeBillingEffects()
    }

    override fun onDestroyView() {
        Logger.i { "AdRemoveBottomSheet onDestroyView" }
        billingManager.endConnection()
        binding.recyclerViewRemoveAds.adapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(this::class.simpleName.toString())
    }

    private fun initViews() {
        binding.recyclerViewRemoveAds.adapter = removeAdsAdapter
    }

    private fun observeStates() = adRemoveViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                binding.loadingView.showLoading(loading)
                removeAdsAdapter.submitList(adRemoveTypes)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = adRemoveViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "AdRemoveBottomSheet observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is AdRemoveEffect.LaunchRemoveAdFlow -> {
                    if (viewEffect.removeAdType == RemoveAdType.VIDEO) {
                        showDialog(
                            activity = requireActivity(),
                            title = R.string.txt_remove_ads,
                            message = R.string.txt_remove_ads_text,
                            positiveButton = R.string.txt_watch
                        ) {
                            adRemoveViewModel.showLoadingView(true)
                            showRewardedAd()
                        }
                    } else {
                        billingManager.launchBillingFlow(
                            requireActivity(),
                            viewEffect.removeAdType.data.id
                        )
                    }
                }
                is AdRemoveEffect.AdsRemoved -> {
                    if (viewEffect.removeAdType == RemoveAdType.VIDEO ||
                        viewEffect.isRestorePurchase
                    ) {
                        restartActivity()
                    } else {
                        billingManager.acknowledgePurchase()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeBillingEffects() = billingManager.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "AdRemoveBottomSheet observeBillingEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                BillingEffect.SuccessfulPurchase -> restartActivity()
                is BillingEffect.RestorePurchase -> adRemoveViewModel.restorePurchase(
                    viewEffect.purchaseHistoryRecordList.toOldPurchaseList()
                )
                is BillingEffect.AddPurchaseMethods -> adRemoveViewModel.addPurchaseMethods(
                    viewEffect.purchaseMethodList.toRemoveAdDataList()
                )
                is BillingEffect.UpdateAddFreeDate -> adRemoveViewModel.updateAddFreeDate(
                    RemoveAdType.getById(viewEffect.id)
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun showRewardedAd() {
        adManager.showRewardedAd(
            activity = requireActivity(),
            adId = getString(R.string.android_rewarded_ad_unit_id),
            onAdFailedToLoad = {
                adRemoveViewModel.showLoadingView(false)
                view?.let { showSnack(it, R.string.error_text_unknown) }
            },
            onAdLoaded = {
                adRemoveViewModel.showLoadingView(false)
            },
            onReward = {
                adRemoveViewModel.updateAddFreeDate(RemoveAdType.VIDEO)
            }
        )
    }

    private fun restartActivity() = activity?.run {
        finish()
        startActivity(intent)
    }
}

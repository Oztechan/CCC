/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.adremove

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.ad.showRewardedAd
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.billing.BillingEffect
import com.github.mustafaozhan.billing.BillingManager
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.android.util.toPurchaseHistoryList
import com.github.mustafaozhan.ccc.android.util.toRemoveAdDataList
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.mycurrencies.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetAdRemoveBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class AdRemoveBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetAdRemoveBinding>() {

    private val billingManager: BillingManager by inject()
    private val adRemoveViewModel: AdRemoveViewModel by viewModel()

    private lateinit var removeAdsAdapter: RemoveAdsAdapter

    override fun getViewBinding() = BottomSheetAdRemoveBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "AdRemoveBottomSheet onViewCreated" }
        billingManager.setupBillingClient(
            viewLifecycleOwner.lifecycleScope,
            RemoveAdType.getSkuList()
        )
        initViews()
        observeStates()
        observeEffects()
        observeBillingEffects()
    }

    override fun onDestroyView() {
        billingManager.endConnection()
        binding.recyclerViewBar.adapter = null
        super.onDestroyView()
    }

    private fun initViews() {
        removeAdsAdapter = RemoveAdsAdapter(adRemoveViewModel.event)
        binding.recyclerViewBar.adapter = removeAdsAdapter
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
            kermit.d { "AdRemoveBottomSheet observeEffect ${viewEffect::class.simpleName}" }
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
                            viewEffect.removeAdType.data.skuId
                        )
                    }
                }
                is AdRemoveEffect.AdsRemoved -> {
                    if (viewEffect.removeAdType == RemoveAdType.VIDEO) {
                        restartActivity()
                    } else {
                        billingManager.acknowledgePurchase()
                    }
                }
                AdRemoveEffect.AlreadyAdFree -> showSnack(
                    requireView(),
                    R.string.txt_ads_already_disabled
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeBillingEffects() = billingManager.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            kermit.d { "AdRemoveBottomSheet observeBillingEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                BillingEffect.SuccessfulPurchase -> restartActivity()
                is BillingEffect.RestorePurchase -> adRemoveViewModel.restorePurchase(
                    viewEffect.purchaseHistoryRecordList.toPurchaseHistoryList()
                )
                is BillingEffect.AddInAppBillingMethods -> adRemoveViewModel.addInAppBillingMethods(
                    viewEffect.skuDetailsList.toRemoveAdDataList()
                )
                is BillingEffect.UpdateAddFreeDate -> adRemoveViewModel.updateAddFreeDate(
                    RemoveAdType.getBySku(viewEffect.sku)
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun showRewardedAd() = requireActivity().showRewardedAd(
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

    private fun restartActivity() = activity?.run {
        finish()
        startActivity(intent)
    }
}

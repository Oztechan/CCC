/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.adremove

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.billing.BillingManager
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.logmob.kermit
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
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
        initViews()
        observeStates()
        observeEffect()
        billingManager.setupBillingClient(viewLifecycleOwner.lifecycleScope)
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

    private fun observeEffect() = adRemoveViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            kermit.d { "AdRemoveBottomSheet observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is AdRemoveEffect.LaunchRemoveAdFlow -> {
                    if (viewEffect.removeAdType == RemoveAdType.VIDEO) {
                        prepareRewardedAdFlow()
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

    private fun prepareRewardedAdFlow() {
        showDialog(
            requireActivity(),
            R.string.txt_remove_ads,
            R.string.txt_remove_ads_text,
            R.string.txt_watch
        ) {
            adRemoveViewModel.showLoadingView(true)
            prepareRewardedAd()
        }
    }

    private fun restartActivity() = activity?.run {
        finish()
        startActivity(intent)
    }

    private fun prepareRewardedAd() = context?.applicationContext?.let { applicationContext ->
        RewardedAd.load(
            applicationContext,
            getString(R.string.android_rewarded_ad_unit_id),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    kermit.d { "AdRemoveBottomSheet onRewardedAdFailedToLoad" }
                    adRemoveViewModel.showLoadingView(false)
                    view?.let { showSnack(it, R.string.error_text_unknown) }
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    adRemoveViewModel.showLoadingView(false)
                    kermit.d { "AdRemoveBottomSheet onRewardedAdLoaded" }

                    activity?.let {
                        rewardedAd.show(it) {
                            kermit.d { "AdRemoveBottomSheet onUserEarnedReward" }
                            adRemoveViewModel.updateAddFreeDate(RemoveAdType.VIDEO)
                        }
                    }
                }
            }
        )
    }
}

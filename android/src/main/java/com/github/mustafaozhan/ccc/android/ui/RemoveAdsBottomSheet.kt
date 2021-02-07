/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.View
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.RemoveAdsViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetRemoveAdsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RemoveAdsBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetRemoveAdsBinding>() {

    private val removeAdsViewModel: RemoveAdsViewModel by viewModel()

    override fun bind() {
        binding = BottomSheetRemoveAdsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "RemoveAdsBottomSheet onViewCreated" }

        showDialog(
            requireActivity(),
            R.string.remove_ads,
            R.string.remove_ads_text,
            R.string.watch
        ) {
            removeAdsViewModel.showLoadingView(true)
            prepareRewardedAd()
        }
    }

    private fun prepareRewardedAd() = RewardedAd.load(
        requireContext(),
        getString(R.string.rewarded_ad_unit_id),
        AdRequest.Builder().build(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) = context?.let {
                kermit.d { "SettingsFragment onRewardedAdFailedToLoad" }
                removeAdsViewModel.showLoadingView(false)
                Toast.show(it, R.string.error_text_unknown)
            }.toUnit()

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                removeAdsViewModel.showLoadingView(false)
                kermit.d { "SettingsFragment onRewardedAdLoaded" }

                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() =
                        kermit.d { "SettingsFragment onAdDismissedFullScreenContent" }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) =
                        context?.let {
                            kermit.d { "SettingsFragment onRewardedAdFailedToShow" }
                            Toast.show(it, R.string.error_text_unknown)
                        }.toUnit()

                    override fun onAdShowedFullScreenContent() =
                        kermit.d { "SettingsFragment onAdShowedFullScreenContent" }
                }

                rewardedAd.show(requireActivity()) {
                    kermit.d { "SettingsFragment onUserEarnedReward" }
                    removeAdsViewModel.updateAddFreeDate()
                    activity?.run {
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
    )
}

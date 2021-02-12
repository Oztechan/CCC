/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetAdRemoveBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdRemoveBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetAdRemoveBinding>() {

    private val adRemoveViewModel: AdRemoveViewModel by viewModel()

    override fun bind() {
        binding = BottomSheetAdRemoveBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "AdRemoveBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    private fun initViews() = with(binding) {
        itemWatchVideo.txtAction.text = getString(R.string.txt_watch_video)
        itemWatchVideo.txtReward.text = getString(R.string.txt_watch_video_reward)

        itemMonth.txtAction.text = getString(R.string.txt_pay_one_euro)
        itemMonth.txtReward.text = getString(R.string.txt_pay_one_euro_reward)

        itemQuarter.txtAction.text = getString(R.string.txt_pay_two_euro)
        itemQuarter.txtReward.text = getString(R.string.txt_pay_two_euro_reward)

        itemHalfYear.txtAction.text = getString(R.string.txt_pay_three_euro)
        itemHalfYear.txtReward.text = getString(R.string.txt_pay_three_euro_reward)

        itemYear.txtAction.text = getString(R.string.txt_pay_five_euro)
        itemYear.txtReward.text = getString(R.string.txt_pay_five_euro_reward)
    }

    private fun observeStates() = lifecycleScope.launchWhenStarted {
        adRemoveViewModel.state.collect {
            with(it) {
                binding.loadingView.visibleIf(loading)
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        adRemoveViewModel.effect.collect { viewEffect ->
            kermit.d { "AdRemoveBottomSheet observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is AdRemoveEffect.WatchVideo -> showDialog(
                    requireActivity(),
                    R.string.txt_remove_ads,
                    R.string.txt_remove_ads_text,
                    R.string.txt_watch
                ) {
                    adRemoveViewModel.showLoadingView(true)
                    prepareRewardedAd()
                }
            }
        }
    }

    private fun setListeners() = with(binding) {
        itemWatchVideo.root.setOnClickListener {
            adRemoveViewModel.event.onWatchVideoClick()
        }
    }

    private fun prepareRewardedAd() = RewardedAd.load(
        requireContext(),
        getString(R.string.rewarded_ad_unit_id),
        AdRequest.Builder().build(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) = context?.let {
                kermit.d { "AdRemoveBottomSheet onRewardedAdFailedToLoad" }
                adRemoveViewModel.showLoadingView(false)
                Toast.show(it, R.string.error_text_unknown)
            }.toUnit()

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                adRemoveViewModel.showLoadingView(false)
                kermit.d { "AdRemoveBottomSheet onRewardedAdLoaded" }

                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() =
                        kermit.d { "AdRemoveBottomSheet onAdDismissedFullScreenContent" }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) =
                        context?.let {
                            kermit.d { "AdRemoveBottomSheet onRewardedAdFailedToShow" }
                            Toast.show(it, R.string.error_text_unknown)
                        }.toUnit()

                    override fun onAdShowedFullScreenContent() =
                        kermit.d { "AdRemoveBottomSheet onAdShowedFullScreenContent" }
                }

                rewardedAd.show(requireActivity()) {
                    kermit.d { "AdRemoveBottomSheet onUserEarnedReward" }
                    adRemoveViewModel.updateAddFreeDate()
                    activity?.run {
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
    )
}

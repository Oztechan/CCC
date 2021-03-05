/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.model.BillingPeriod
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveViewModel
import com.github.mustafaozhan.logmob.kermit
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

class AdRemoveBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetAdRemoveBinding>(),
    PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient

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
        setupBillingClient()
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
                is AdRemoveEffect.Billing -> launchBilling(viewEffect.period)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun launchBilling(period: BillingPeriod) {

        if (billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(BillingPeriod.values().map { it.skuId })
                .setType(BillingClient.SkuType.INAPP)
                .build()
            billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    kermit.d { "AdRemoveBottomSheet launchBilling querySkuDetailsAsync, responseCode: ${billingResult.responseCode}" }
                    skuDetailsList?.let {
                        val billingFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(it.first())
                            .build()
                        billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
                    }
                } else {
                    kermit.d { "AdRemoveBottomSheet launchBilling Can't querySkuDetailsAsync, responseCode: ${billingResult.responseCode}" }
                }
            }
        } else {
            kermit.d { "AdRemoveBottomSheet launchBilling Billing Client not ready" }
        }
    }

    private fun setupBillingClient() {
        billingClient = BillingClient
            .newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener(this)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    kermit.d { "AdRemoveBottomSheet onBillingSetupFinished OK" }
                } else {
                    kermit.d { "AdRemoveBottomSheet onBillingSetupFinished ${billingResult.responseCode}" }
                }
            }

            override fun onBillingServiceDisconnected() {
                kermit.d { "AdRemoveBottomSheet onBillingServiceDisconnected" }
            }
        })
    }

    private fun setListeners() = with(binding) {
        itemWatchVideo.root.setOnClickListener {
            adRemoveViewModel.event.onWatchVideoClick()
        }
        itemMonth.root.setOnClickListener {
            adRemoveViewModel.event.onBillingClick(BillingPeriod.MONTH)
        }
        itemQuarter.root.setOnClickListener {
            adRemoveViewModel.event.onBillingClick(BillingPeriod.QUARTER)
        }
        itemHalfYear.root.setOnClickListener {
            adRemoveViewModel.event.onBillingClick(BillingPeriod.HALF_YEAR)
        }
        itemYear.root.setOnClickListener {
            adRemoveViewModel.event.onBillingClick(BillingPeriod.YEAR)
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

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        kermit.d { "AdRemoveBottomSheet onPurchasesUpdated" }
    }
}

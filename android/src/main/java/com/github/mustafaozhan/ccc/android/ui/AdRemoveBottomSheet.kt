/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.util.Toast
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveEvent
import com.github.mustafaozhan.ccc.client.viewmodel.AdRemoveViewModel
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.whether
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetAdRemoveBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemAdRemoveBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class AdRemoveBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetAdRemoveBinding>(),
    PurchaseHistoryResponseListener,
    PurchasesUpdatedListener,
    SkuDetailsResponseListener,
    BillingClientStateListener {

    private lateinit var billingClient: BillingClient

    private val adRemoveViewModel: AdRemoveViewModel by viewModel()

    private lateinit var removeAdsAdapter: RemoveAdsAdapter

    private lateinit var skuDetails: List<SkuDetails>

    override fun bind() {
        binding = BottomSheetAdRemoveBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "AdRemoveBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setupBillingClient()
    }

    private fun initViews() {
        removeAdsAdapter = RemoveAdsAdapter(adRemoveViewModel.event)
        binding.recyclerViewBar.adapter = removeAdsAdapter
    }

    private fun observeStates() = lifecycleScope.launchWhenStarted {
        adRemoveViewModel.state.collect {
            with(it) {
                binding.loadingView.visibleIf(loading)
                removeAdsAdapter.submitList(adRemoveTypes)
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        adRemoveViewModel.effect.collect { viewEffect ->
            kermit.d { "AdRemoveBottomSheet observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is AdRemoveEffect.RemoveAd -> {
                    if (viewEffect.removeAdType == RemoveAdType.VIDEO) {
                        showDialog(
                            requireActivity(),
                            R.string.txt_remove_ads,
                            R.string.txt_remove_ads_text,
                            R.string.txt_watch
                        ) {
                            adRemoveViewModel.showLoadingView(true)
                            prepareRewardedAd()
                        }
                    } else {
                        launchBillingFlow(viewEffect.removeAdType.skuId)
                    }
                }
                AdRemoveEffect.RestartActivity -> activity?.run {
                    finish()
                    startActivity(intent)
                }
            }
        }
    }

    private fun setupBillingClient() {
        adRemoveViewModel.showLoadingView(true)
        billingClient = BillingClient
            .newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(this)
    }

    private fun launchBillingFlow(skuId: String) = skuDetails
        .firstOrNull { it.sku == skuId }
        ?.let {
            val billingFlowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(it)
                .build()
            billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
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
                    adRemoveViewModel.updateAddFreeDate(RemoveAdType.VIDEO)
                }
            }
        }
    )

    override fun onSkuDetailsResponse(
        billingResult: BillingResult,
        skuDetailsList: MutableList<SkuDetails>?
    ) {
        kermit.d { "AdRemoveBottomSheet onBillingSetupFinished querySkuDetailsAsync" }
        skuDetailsList
            ?.whether { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
            ?.apply {
                skuDetails = this
                map { Triple(it.sku, it.price, it.description) }.let {
                    adRemoveViewModel.addInAppBillingMethods(it)
                }
            } ?: run {
            adRemoveViewModel.showLoadingView(false)
        }
    }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) = purchaseHistoryList.also {
        kermit.d { "AdRemoveBottomSheet onPurchaseHistoryResponse" }
    }?.minByOrNull { it.purchaseTime }
        ?.whether { historyRecord ->
            RemoveAdType.values()
                .map { removeAdType -> removeAdType.skuId }
                .any { skuId -> skuId == historyRecord.sku }
        }?.let { historyRecord ->
            adRemoveViewModel.validatePurchaseHistory(
                Pair(historyRecord.sku, historyRecord.purchaseTime)
            )
        }.toUnit()

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) {
        kermit.d { "AdRemoveBottomSheet onPurchasesUpdated" }
        purchaseList?.firstOrNull()?.let { purchase ->
            RemoveAdType.values().firstOrNull { it.skuId == purchase.sku }?.let {
                adRemoveViewModel.updateAddFreeDate(it)
            }
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) = billingClient
        .also {
            kermit.d { "AdRemoveBottomSheet onBillingSetupFinished" }
            it.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)
        }
        .whether(
            { isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {

            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(
                    RemoveAdType.values()
                        .map { it.skuId }
                        .filter { it.isNotEmpty() }
                )
                .setType(BillingClient.SkuType.INAPP)
                .build()
            querySkuDetailsAsync(skuDetailsParams, this@AdRemoveBottomSheet)
        }.toUnit()

    override fun onBillingServiceDisconnected() {
        kermit.d { "AdRemoveBottomSheet onBillingServiceDisconnected" }
        adRemoveViewModel.showLoadingView(false)
    }
}

class RemoveAdsAdapter(
    private val removeAdsEvent: AdRemoveEvent
) : BaseVBRecyclerViewAdapter<RemoveAdType, ItemAdRemoveBinding>(RemoveAdDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorVBViewHolder(
        ItemAdRemoveBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class CalculatorVBViewHolder(itemBinding: ItemAdRemoveBinding) :
        BaseVBViewHolder<RemoveAdType, ItemAdRemoveBinding>(itemBinding) {

        override fun onItemBind(item: RemoveAdType) = with(itemBinding) {
            root.setOnClickListener { removeAdsEvent.onAdRemoveItemClick(item) }
            txtReward.text = item.reward
            txtCost.text = item.cost
        }
    }

    class RemoveAdDiffer : DiffUtil.ItemCallback<RemoveAdType>() {
        override fun areItemsTheSame(oldItem: RemoveAdType, newItem: RemoveAdType) = false

        override fun areContentsTheSame(oldItem: RemoveAdType, newItem: RemoveAdType) = false
    }
}

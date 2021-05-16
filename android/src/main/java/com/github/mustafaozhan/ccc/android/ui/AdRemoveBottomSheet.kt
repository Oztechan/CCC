/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
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
import com.github.mustafaozhan.ccc.android.util.showDialog
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.unitOrNull
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEffect
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveEvent
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveViewModel
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.google.android.gms.ads.AdRequest
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

    override fun getViewBinding() = BottomSheetAdRemoveBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "AdRemoveBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setupBillingClient()
    }

    override fun onDestroyView() {
        billingClient.endConnection()
        binding.recyclerViewBar.adapter = null
        super.onDestroyView()
    }

    private fun initViews() {
        removeAdsAdapter = RemoveAdsAdapter(adRemoveViewModel.event)
        binding.recyclerViewBar.adapter = removeAdsAdapter
    }

    private fun observeStates() = viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
        adRemoveViewModel.state.collect {
            with(it) {
                binding.loadingView.showLoading(loading)
                removeAdsAdapter.submitList(adRemoveTypes)
            }
        }
    }

    private fun observeEffect() = viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
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
                        launchBillingFlow(viewEffect.removeAdType.data.skuId)
                    }
                }
                AdRemoveEffect.AlreadyAdFree -> showSnack(
                    requireView(),
                    R.string.txt_ads_already_disabled
                )
                AdRemoveEffect.RestartActivity -> restartActivity()
            }
        }
    }

    private fun restartActivity() = activity?.run {
        finish()
        startActivity(intent)
    }

    private fun setupBillingClient() {
        adRemoveViewModel.showLoadingView(true)
        billingClient = BillingClient
            .newBuilder(requireContext().applicationContext)
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

    private fun prepareRewardedAd() = context?.applicationContext?.let { applicationContext ->
        RewardedAd.load(
            applicationContext,
            getString(R.string.rewarded_ad_unit_id),
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

    override fun onSkuDetailsResponse(
        billingResult: BillingResult,
        skuDetailsList: MutableList<SkuDetails>?
    ) = skuDetailsList
        .also { kermit.d { "AdRemoveBottomSheet onSkuDetailsResponse ${billingResult.responseCode}" } }
        ?.whether { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        ?.let { detailsList ->
            this.skuDetails = detailsList
            adRemoveViewModel.addInAppBillingMethods(detailsList.map {
                RemoveAdData(it.price, it.description, it.sku)
            })
        }.unitOrNull()
        ?: run { adRemoveViewModel.showLoadingView(false) }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) = purchaseHistoryList
        .also { kermit.d { "AdRemoveBottomSheet onPurchaseHistoryResponse ${billingResult.responseCode}" } }
        ?.mapNotNull { historyRecord ->
            RemoveAdType.getBySku(historyRecord.sku)?.let {
                PurchaseHistory(historyRecord.purchaseTime, it)
            }
        }?.let { adRemoveViewModel.restorePurchase(it) }
        .toUnit()

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) = purchaseList
        .also { kermit.d { "AdRemoveBottomSheet onPurchasesUpdated ${billingResult.responseCode}" } }
        ?.firstOrNull()
        ?.mapTo { RemoveAdType.getBySku(sku) }
        ?.let { adRemoveViewModel.updateAddFreeDate(it) }
        .toUnit()

    override fun onBillingSetupFinished(billingResult: BillingResult) = billingClient
        .also {
            kermit.d { "AdRemoveBottomSheet onBillingSetupFinished ${billingResult.responseCode}" }
            adRemoveViewModel.showLoadingView(false)
            it.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)
        }.whether(
            { isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {
            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(RemoveAdType.getSkuList())
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
            txtReward.text = item.data.reward
            txtCost.text = item.data.cost
        }
    }

    class RemoveAdDiffer : DiffUtil.ItemCallback<RemoveAdType>() {
        override fun areItemsTheSame(oldItem: RemoveAdType, newItem: RemoveAdType) = false

        override fun areContentsTheSame(oldItem: RemoveAdType, newItem: RemoveAdType) = false
    }
}

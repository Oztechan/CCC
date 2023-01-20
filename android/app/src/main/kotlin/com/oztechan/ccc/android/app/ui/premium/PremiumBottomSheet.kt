/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.android.app.ui.premium

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.ScreenName
import com.oztechan.ccc.android.app.R
import com.oztechan.ccc.android.app.databinding.BottomSheetPremiumBinding
import com.oztechan.ccc.android.app.util.showDialog
import com.oztechan.ccc.android.app.util.showSnack
import com.oztechan.ccc.android.app.util.toOldPurchaseList
import com.oztechan.ccc.android.app.util.toPremiumDataList
import com.oztechan.ccc.android.app.util.visibleIf
import com.oztechan.ccc.billing.BillingEffect
import com.oztechan.ccc.billing.BillingManager
import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.client.viewmodel.premium.PremiumEffect
import com.oztechan.ccc.client.viewmodel.premium.PremiumViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class PremiumBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetPremiumBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val billingManager: BillingManager by inject()
    private val premiumViewModel: PremiumViewModel by viewModel()

    private val premiumAdapter: PremiumAdapter by lazy {
        PremiumAdapter(premiumViewModel.event)
    }

    override fun getViewBinding() = BottomSheetPremiumBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "PremiumBottomSheet onViewCreated" }
        billingManager.startConnection(viewLifecycleOwner.lifecycleScope, PremiumType.getPurchaseIds())
        initViews()
        observeStates()
        observeEffects()
        observeBillingEffects()
    }

    override fun onDestroyView() {
        Logger.i { "PremiumBottomSheet onDestroyView" }
        billingManager.endConnection()
        binding.recyclerViewPremium.adapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(ScreenName.Premium)
    }

    private fun initViews() {
        binding.recyclerViewPremium.adapter = premiumAdapter
    }

    private fun observeStates() = premiumViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                binding.loadingView.visibleIf(loading, true)
                premiumAdapter.submitList(premiumTypes)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = premiumViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "PremiumBottomSheet observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is PremiumEffect.LaunchActivatePremiumFlow -> {
                    if (viewEffect.premiumType == PremiumType.VIDEO) {
                        activity?.showDialog(
                            title = R.string.txt_premium,
                            message = R.string.txt_premium_text,
                            positiveButton = R.string.txt_watch
                        ) {
                            premiumViewModel.showLoadingView(true)
                            showRewardedAd()
                        }
                    } else {
                        billingManager.launchBillingFlow(requireActivity(), viewEffect.premiumType.data.id)
                    }
                }

                is PremiumEffect.PremiumActivated -> {
                    if (viewEffect.premiumType == PremiumType.VIDEO || viewEffect.isRestorePurchase) {
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
            Logger.i { "PremiumBottomSheet observeBillingEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                BillingEffect.SuccessfulPurchase -> restartActivity()
                is BillingEffect.RestorePurchase -> premiumViewModel.restorePurchase(
                    viewEffect.purchaseHistoryRecordRecordList.toOldPurchaseList()
                )

                is BillingEffect.AddPurchaseMethods -> premiumViewModel.addPurchaseMethods(
                    viewEffect.productDetailsList.toPremiumDataList()
                )

                is BillingEffect.UpdatePremiumEndDate -> premiumViewModel.updatePremiumEndDate(
                    PremiumType.getById(viewEffect.id)
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun showRewardedAd() {
        adManager.showRewardedAd(
            activity = requireActivity(),
            adId = getString(R.string.android_rewarded_ad_unit_id),
            onAdFailedToLoad = {
                premiumViewModel.showLoadingView(false)
                view?.showSnack(R.string.error_text_unknown)
            },
            onAdLoaded = {
                premiumViewModel.showLoadingView(false)
            },
            onReward = {
                premiumViewModel.updatePremiumEndDate(PremiumType.VIDEO)
            }
        )
    }

    private fun restartActivity() = activity?.run {
        finish()
        startActivity(intent)
    }
}

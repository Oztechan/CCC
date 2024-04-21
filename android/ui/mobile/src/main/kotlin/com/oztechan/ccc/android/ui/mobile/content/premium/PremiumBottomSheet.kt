/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.android.ui.mobile.content.premium

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.submob.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.core.billing.BillingEffect
import com.oztechan.ccc.android.core.billing.BillingManager
import com.oztechan.ccc.android.ui.mobile.BuildConfig
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.databinding.BottomSheetPremiumBinding
import com.oztechan.ccc.android.ui.mobile.util.resolveAndStartIntent
import com.oztechan.ccc.android.ui.mobile.util.showDialog
import com.oztechan.ccc.android.ui.mobile.util.showSnack
import com.oztechan.ccc.android.ui.mobile.util.toOldPurchaseList
import com.oztechan.ccc.android.ui.mobile.util.toPremiumDataList
import com.oztechan.ccc.android.ui.mobile.util.visibleIf
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.viewmodel.premium.PremiumEffect
import com.oztechan.ccc.client.viewmodel.premium.PremiumViewModel
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        billingManager.startConnection(
            viewLifecycleOwner,
            PremiumType.getPurchaseIds()
        )
        binding.initViews()
        binding.observeStates()
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

    private fun BottomSheetPremiumBinding.initViews() {
        recyclerViewPremium.adapter = premiumAdapter
    }

    private fun BottomSheetPremiumBinding.observeStates() = premiumViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                loadingView.visibleIf(loading, true)
                premiumAdapter.submitList(premiumTypes)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = premiumViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "PremiumBottomSheet observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is PremiumEffect.LaunchActivatePremiumFlow -> if (viewEffect.premiumType == PremiumType.VIDEO) {
                    activity?.showDialog(
                        title = R.string.txt_premium,
                        message = R.string.txt_premium_text,
                        positiveButton = R.string.txt_watch
                    ) {
                        showRewardedAd()
                    }
                } else {
                    billingManager.launchBillingFlow(
                        requireActivity(),
                        viewEffect.premiumType.data.id
                    )
                }

                is PremiumEffect.PremiumActivated -> if (
                    viewEffect.premiumType == PremiumType.VIDEO ||
                    viewEffect.isRestorePurchase
                ) {
                    restartActivity()
                } else {
                    billingManager.acknowledgePurchase()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeBillingEffects() = billingManager.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "PremiumBottomSheet observeBillingEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                BillingEffect.SuccessfulPurchase -> restartActivity()
                is BillingEffect.RestorePurchase -> premiumViewModel.event.onRestorePurchase(
                    viewEffect.purchaseHistoryRecordRecordList.toOldPurchaseList()
                )

                is BillingEffect.AddPurchaseMethods -> premiumViewModel.event.onAddPurchaseMethods(
                    viewEffect.productDetailsList.toPremiumDataList()
                )

                is BillingEffect.UpdatePremiumEndDate -> premiumViewModel.onPremiumActivated(
                    PremiumType.getById(viewEffect.id)
                )

                BillingEffect.BillingUnavailable -> premiumViewModel.event.onPremiumActivationFailed()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun showRewardedAd() {
        adManager.showRewardedAd(
            activity = requireActivity(),
            adId = if (BuildConfig.DEBUG) {
                getString(R.string.rewarded_ad_unit_id_debug)
            } else {
                getString(R.string.rewarded_ad_unit_id_release)
            },
            onAdFailedToLoad = {
                view?.showSnack(R.string.error_text_unknown)
                premiumViewModel.event.onPremiumActivationFailed()
            },
            onReward = {
                premiumViewModel.event.onPremiumActivated(PremiumType.VIDEO)
            }
        )
    }

    private fun restartActivity() = activity?.run {
        finish()
        resolveAndStartIntent(intent)
    }
}

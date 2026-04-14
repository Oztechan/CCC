/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.selectcurrency

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import co.touchlab.kermit.Logger
import com.github.submob.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.ui.mobile.BuildConfig
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.databinding.BottomSheetSelectCurrencyBinding
import com.oztechan.ccc.android.ui.mobile.util.buildBanner
import com.oztechan.ccc.android.ui.mobile.util.destroyBanner
import com.oztechan.ccc.android.ui.mobile.util.visibleIf
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyEffect
import com.oztechan.ccc.client.viewmodel.selectcurrency.SelectCurrencyViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectCurrencyBottomSheet :
    BaseVBBottomSheetDialogFragment<BottomSheetSelectCurrencyBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val viewModel: SelectCurrencyViewModel by viewModel()

    private val selectCurrencyAdapter: SelectCurrencyAdapter by lazy {
        SelectCurrencyAdapter(viewModel.event)
    }

    override fun getViewBinding() = BottomSheetSelectCurrencyBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            skipCollapsed = true
        }
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.layoutParams
            ?.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "SelectCurrencyBottomSheet onViewCreated" }
        val behavior = (dialog as? BottomSheetDialog)?.behavior
        binding.recyclerViewSelectCurrency.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    behavior?.isDraggable = !recyclerView.canScrollVertically(-1)
                }
            }
        )
        binding.initViews()
        binding.observeStates()
        binding.setListeners()
        observeEffects()
    }

    override fun onDestroyView() {
        Logger.i { "SelectCurrencyBottomSheet onDestroyView" }
        binding.adViewContainer.destroyBanner()
        binding.recyclerViewSelectCurrency.adapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        Logger.i { "SelectCurrencyBottomSheet onResume" }
        analyticsManager.trackScreen(ScreenName.SelectCurrency)
    }

    private fun BottomSheetSelectCurrencyBinding.initViews() {
        recyclerViewSelectCurrency.adapter = selectCurrencyAdapter

        adViewContainer.buildBanner(
            adManager = adManager,
            adId = if (BuildConfig.DEBUG) {
                getString(R.string.banner_ad_unit_id_select_currency_debug)
            } else {
                getString(R.string.banner_ad_unit_id_select_currency_release)
            },
            shouldShowAd = viewModel.state.value.isBannerAdVisible
        )
    }

    private fun BottomSheetSelectCurrencyBinding.observeStates() = viewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                selectCurrencyAdapter.submitList(currencyList)

                loadingView.visibleIf(loading, true)

                txtNoEnoughCurrency.text = getString(
                    if (it.enoughCurrency) {
                        R.string.txt_update_favorite_currencies
                    } else {
                        R.string.choose_at_least_two_currency
                    }
                )
                btnSelect.text = getString(
                    if (it.enoughCurrency) R.string.update else R.string.select
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = viewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "SelectCurrencyBottomSheet observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is SelectCurrencyEffect.CurrencySelected -> dismissDialog()

                SelectCurrencyEffect.OpenCurrencies -> navigate(
                    R.id.selectCurrencyBottomSheet,
                    SelectCurrencyBottomSheetDirections.actionSelectCurrencyBottomSheetToCurrenciesFragment()
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun BottomSheetSelectCurrencyBinding.setListeners() = btnSelect.setOnClickListener {
        viewModel.event.onSelectClick()
    }
}

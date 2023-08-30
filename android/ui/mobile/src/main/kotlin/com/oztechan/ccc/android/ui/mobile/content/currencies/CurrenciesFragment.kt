/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.currencies

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import co.touchlab.kermit.Logger
import com.github.submob.basemob.fragment.BaseVBFragment
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.ui.mobile.BuildConfig
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.content.calculator.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.oztechan.ccc.android.ui.mobile.databinding.FragmentCurrenciesBinding
import com.oztechan.ccc.android.ui.mobile.util.destroyBanner
import com.oztechan.ccc.android.ui.mobile.util.hideKeyboard
import com.oztechan.ccc.android.ui.mobile.util.setBannerAd
import com.oztechan.ccc.android.ui.mobile.util.setNavigationResult
import com.oztechan.ccc.android.ui.mobile.util.showSnack
import com.oztechan.ccc.android.ui.mobile.util.visibleIf
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrenciesFragment : BaseVBFragment<FragmentCurrenciesBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val currenciesViewModel: CurrenciesViewModel by viewModel()

    private val currenciesAdapter: CurrenciesAdapter by lazy {
        CurrenciesAdapter(currenciesViewModel.event)
    }

    override fun getViewBinding() = FragmentCurrenciesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "CurrenciesFragment onViewCreated" }
        binding.initViews()
        binding.observeStates()
        binding.setListeners()
        observeEffects()
    }

    override fun onDestroyView() {
        Logger.i { "CurrenciesFragment onDestroyView" }
        binding.adViewContainer.destroyBanner()
        binding.recyclerViewCurrencies.adapter = null
        super.onDestroyView()
    }

    private fun FragmentCurrenciesBinding.initViews() {
        setSpanByOrientation(resources.configuration.orientation)

        with(recyclerViewCurrencies) {
            setHasFixedSize(true)
            adapter = currenciesAdapter
        }
    }

    private fun FragmentCurrenciesBinding.observeStates() = currenciesViewModel.state
        .flowWithLifecycle(lifecycle)
        .onStart {
            adViewContainer.setBannerAd(
                adManager = adManager,
                adId = if (BuildConfig.DEBUG) {
                    getString(R.string.banner_ad_unit_id_currencies_debug)
                } else {
                    getString(R.string.banner_ad_unit_id_currencies_release)
                },
                shouldShowAd = currenciesViewModel.state.value.isBannerAdVisible
            )
        }
        .onEach {
            with(it) {
                currenciesAdapter.submitList(currencyList)

                loadingView.visibleIf(loading, true)

                btnDone.visibleIf(isOnboardingVisible)
                txtSelectCurrencies.visibleIf(isOnboardingVisible)

                with(layoutCurrenciesToolbar) {
                    searchView.visibleIf(!selectionVisibility)
                    txtCurrenciesToolbar.visibleIf(!selectionVisibility)
                    btnSelectAll.visibleIf(selectionVisibility)
                    btnDeSelectAll.visibleIf(selectionVisibility)
                    backButton.visibleIf(!isOnboardingVisible || selectionVisibility)

                    backButton.setBackgroundResource(
                        if (selectionVisibility) R.drawable.ic_close else R.drawable.ic_back
                    )
                    toolbarFragmentCurrencies.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (selectionVisibility) R.color.background_weak else R.color.background_strong
                        )
                    )
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = currenciesViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "CurrenciesFragment observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                CurrenciesEffect.FewCurrency -> view?.showSnack(R.string.choose_at_least_two_currency)
                CurrenciesEffect.OpenCalculator -> {
                    navigate(
                        R.id.currenciesFragment,
                        CurrenciesFragmentDirections.actionCurrenciesFragmentToCalculatorFragment()
                    )
                    view?.hideKeyboard()
                }

                CurrenciesEffect.Back -> {
                    findNavController().popBackStack()
                    view?.hideKeyboard()
                }

                is CurrenciesEffect.ChangeBase -> setNavigationResult(
                    R.id.calculatorFragment,
                    viewEffect.newBase,
                    CHANGE_BASE_EVENT
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun FragmentCurrenciesBinding.setListeners() = with(currenciesViewModel.event) {
        btnDone.setOnClickListener { onDoneClick() }

        with(layoutCurrenciesToolbar) {
            backButton.setOnClickListener { onCloseClick() }
            btnSelectAll.setOnClickListener { updateAllCurrenciesState(true) }
            btnDeSelectAll.setOnClickListener { updateAllCurrenciesState(false) }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = false
                override fun onQueryTextChange(newText: String): Boolean {
                    Logger.i { "CurrenciesFragment onQueryTextChange $newText" }
                    currenciesViewModel.event.onQueryChange(newText)
                    return true
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(ScreenName.Currencies)
        Logger.i { "CurrenciesFragment onResume" }
        currenciesViewModel.hideSelectionVisibility()
        currenciesViewModel.event.onQueryChange("")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logger.i { "CurrenciesFragment onConfigurationChanged $newConfig" }
        setSpanByOrientation(newConfig.orientation)
    }

    private fun setSpanByOrientation(orientation: Int) {
        binding.recyclerViewCurrencies.layoutManager = GridLayoutManager(
            requireContext(),
            if (orientation == ORIENTATION_LANDSCAPE) SPAN_LANDSCAPE else SPAN_PORTRAIT
        )
    }

    companion object {
        internal const val SPAN_PORTRAIT = 1
        internal const val SPAN_LANDSCAPE = 3
    }
}

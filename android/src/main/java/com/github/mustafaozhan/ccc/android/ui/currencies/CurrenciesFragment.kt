/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.currencies

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.github.mustafaozhan.ccc.android.util.hideKeyboard
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.showLoading
import com.github.mustafaozhan.ccc.android.util.showSnack
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCurrenciesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrenciesFragment : BaseVBFragment<FragmentCurrenciesBinding>() {

    private val currenciesViewModel: CurrenciesViewModel by viewModel()

    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun getViewBinding() = FragmentCurrenciesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kermit.d { "CurrenciesFragment onViewCreated" }
        initViews()
        observeStates()
        observeEffects()
        setListeners()
    }

    override fun onDestroyView() {
        binding.adViewContainer.removeAllViews()
        binding.recyclerViewCurrencies.adapter = null
        super.onDestroyView()
    }

    private fun initViews() = with(binding) {
        adViewContainer.setAdaptiveBannerAd(
            getString(R.string.android_banner_ad_unit_id_currencies),
            currenciesViewModel.isRewardExpired()
        )
        currenciesAdapter = CurrenciesAdapter(currenciesViewModel.event)
        setSpanByOrientation(resources.configuration.orientation)

        with(recyclerViewCurrencies) {
            setHasFixedSize(true)
            adapter = currenciesAdapter
        }

        btnDone.visibleIf(currenciesViewModel.isFirstRun())
        txtSelectCurrencies.visibleIf(currenciesViewModel.isFirstRun())
    }

    private fun observeStates() = currenciesViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                currenciesAdapter.submitList(currencyList)

                binding.loadingView.showLoading(loading)

                with(binding.layoutCurrenciesToolbar) {
                    searchView.visibleIf(!selectionVisibility)
                    txtCurrenciesToolbar.visibleIf(!selectionVisibility)
                    btnSelectAll.visibleIf(selectionVisibility)
                    btnDeSelectAll.visibleIf(selectionVisibility)
                    backButton.visibleIf(!currenciesViewModel.isFirstRun() || selectionVisibility)

                    backButton.setBackgroundResource(
                        if (selectionVisibility) R.drawable.ic_close else R.drawable.ic_back
                    )
                    toolbarFragmentCurrencies.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (selectionVisibility) R.color.color_background_weak else R.color.color_background_strong
                        )
                    )
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = currenciesViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            kermit.d { "CurrenciesFragment observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                CurrenciesEffect.FewCurrency -> showSnack(
                    requireView(),
                    R.string.choose_at_least_two_currency
                )
                CurrenciesEffect.OpenCalculator -> {
                    navigate(
                        R.id.currenciesFragment,
                        CurrenciesFragmentDirections.actionCurrenciesFragmentToCalculatorFragment()
                    )
                    view?.hideKeyboard()
                }
                CurrenciesEffect.Back -> {
                    getBaseActivity()?.onBackPressed()
                    view?.hideKeyboard()
                }
                is CurrenciesEffect.ChangeBase -> setNavigationResult(
                    R.id.calculatorFragment,
                    viewEffect.newBase,
                    CHANGE_BASE_EVENT
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun setListeners() = with(binding) {
        with(currenciesViewModel.event) {

            btnDone.setOnClickListener { onDoneClick() }

            with(layoutCurrenciesToolbar) {

                backButton.setOnClickListener { onCloseClick() }
                btnSelectAll.setOnClickListener { updateAllCurrenciesState(true) }
                btnDeSelectAll.setOnClickListener { updateAllCurrenciesState(false) }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String) = false
                    override fun onQueryTextChange(newText: String): Boolean {
                        kermit.d { "CurrenciesFragment onQueryTextChange" }
                        currenciesViewModel.event.onQueryChange(newText)
                        return true
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        kermit.d { "CurrenciesFragment onResume" }
        currenciesViewModel.hideSelectionVisibility()
        currenciesViewModel.event.onQueryChange("")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        kermit.d { "CurrenciesFragment onConfigurationChanged" }
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

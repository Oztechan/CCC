/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.currencies

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.Toast.show
import com.github.mustafaozhan.ccc.android.util.hideKeyboard
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.ui.currencies.BackEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.CalculatorEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.FewCurrencyEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCurrenciesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrenciesFragment : BaseVBFragment<FragmentCurrenciesBinding>() {

    companion object {
        internal const val SPAN_PORTRAIT = 1
        internal const val SPAN_LANDSCAPE = 3
    }

    private val vm: CurrenciesViewModel by viewModel()

    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun bind() {
        binding = FragmentCurrenciesBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    private fun initViews() = with(binding) {
        currenciesAdapter = CurrenciesAdapter(vm.useCase.getEvent())
        setSpanByOrientation(resources.configuration.orientation)

        with(recyclerViewCurrencies) {
            setHasFixedSize(true)
            adapter = currenciesAdapter
        }

        btnDone.visibleIf(vm.useCase.isFirstRun())
        txtSelectCurrencies.visibleIf(vm.useCase.isFirstRun())
    }

    private fun observeStates() = with(vm.useCase.state) {
        lifecycleScope.launchWhenStarted {
            currencyList.collect {
                currenciesAdapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            loading.collect {
                binding.loadingView.visibleIf(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            selectionVisibility.collect {
                with(binding.layoutCurrenciesToolbar) {
                    searchView.visibleIf(!it)
                    txtCurrenciesToolbar.visibleIf(!it)
                    btnSelectAll.visibleIf(it)
                    btnDeSelectAll.visibleIf(it)
                    backButton.visibleIf(!vm.useCase.isFirstRun() || it)

                    backButton.setBackgroundResource(if (it) R.drawable.ic_close else R.drawable.ic_back)
                    toolbarFragmentCurrencies.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (it) R.color.color_background_weak else R.color.color_background_strong
                        )
                    )
                }
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        vm.useCase.effect.collect { viewEffect ->
            when (viewEffect) {
                FewCurrencyEffect -> show(requireContext(), R.string.choose_at_least_two_currency)
                CalculatorEffect -> {
                    navigate(
                        R.id.currenciesFragment,
                        CurrenciesFragmentDirections.actionCurrenciesFragmentToCalculatorFragment()
                    )
                    view?.run { hideKeyboard() }
                }
                BackEffect -> {
                    getBaseActivity()?.onBackPressed()
                    view?.run { hideKeyboard() }
                }
                is ChangeBaseNavResultEffect -> {
                    setNavigationResult(
                        R.id.calculatorFragment,
                        viewEffect.newBase,
                        KEY_BASE_CURRENCY
                    )
                }
            }
        }
    }

    private fun setListeners() = with(binding) {
        with(vm.useCase.getEvent()) {

            btnDone.setOnClickListener { onDoneClick() }

            with(layoutCurrenciesToolbar) {

                backButton.setOnClickListener { onCloseClick() }
                btnSelectAll.setOnClickListener { updateAllCurrenciesState(true) }
                btnDeSelectAll.setOnClickListener { updateAllCurrenciesState(false) }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String) = false
                    override fun onQueryTextChange(newText: String) =
                        vm.useCase.filterList(newText)
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_currencies),
            vm.useCase.isRewardExpired()
        )
        vm.useCase.hideSelectionVisibility()
        vm.useCase.filterList("")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setSpanByOrientation(newConfig.orientation)
    }

    private fun setSpanByOrientation(orientation: Int) {
        binding.recyclerViewCurrencies.layoutManager = GridLayoutManager(
            requireContext(),
            if (orientation == ORIENTATION_LANDSCAPE) SPAN_LANDSCAPE else SPAN_PORTRAIT
        )
    }
}

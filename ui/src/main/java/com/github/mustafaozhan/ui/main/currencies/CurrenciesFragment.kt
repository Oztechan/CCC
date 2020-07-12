/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.currencies

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.util.Toast.show
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.setNavigationResult
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.databinding.FragmentCurrenciesBinding
import com.github.mustafaozhan.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ui.main.currencies.CurrenciesData.Companion.SPAN_LANDSCAPE
import com.github.mustafaozhan.ui.main.currencies.CurrenciesData.Companion.SPAN_PORTRAIT
import com.github.mustafaozhan.ui.util.hideKeyboard
import com.github.mustafaozhan.ui.util.setAdaptiveBannerAd
import javax.inject.Inject

class CurrenciesFragment : BaseDBFragment<FragmentCurrenciesBinding>() {

    @Inject
    lateinit var currenciesViewModel: CurrenciesViewModel

    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun bind(container: ViewGroup?): FragmentCurrenciesBinding =
        FragmentCurrenciesBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCurrenciesBinding) {
        binding.vm = currenciesViewModel
        currenciesViewModel.getEvent().let {
            binding.event = it
            currenciesAdapter = CurrenciesAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.layoutCurrenciesToolbar.toolbarFragmentCurrencies)
        initView()
        observeEffect()
    }

    override fun onResume() {
        super.onResume()
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_currencies),
            currenciesViewModel.data.isRewardExpired
        )
        currenciesViewModel.hideSelectionVisibility()
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

    private fun initView() {
        setSpanByOrientation(resources.configuration.orientation)

        with(binding.recyclerViewCurrencies) {
            setHasFixedSize(true)
            adapter = currenciesAdapter
        }

        currenciesViewModel.state.currencyList.reObserve(viewLifecycleOwner, Observer {
            currenciesAdapter.submitList(it)
        })
    }

    private fun observeEffect() = currenciesViewModel.effect
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                FewCurrencyEffect -> show(requireContext(), R.string.choose_at_least_two_currency)
                CalculatorEffect -> {
                    navigate(
                        R.id.currenciesFragment,
                        CurrenciesFragmentDirections.actionCurrenciesFragmentToCalculatorFragment()
                    )
                    requireView().hideKeyboard()
                }
                BackEffect -> {
                    getBaseActivity()?.onBackPressed()
                    requireView().hideKeyboard()
                }
                is ChangeBaseNavResultEffect -> {
                    setNavigationResult(viewEffect.newBase, KEY_BASE_CURRENCY)
                }
            }
        })
}

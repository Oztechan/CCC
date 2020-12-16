/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.adapter.BaseDBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.fragment.BaseDBFragment
import com.github.mustafaozhan.ccc.android.util.Toast.show
import com.github.mustafaozhan.ccc.android.util.hideKeyboard
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.client.ui.currencies.BackEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.CalculatorEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.ChangeBaseNavResultEffect
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesEvent
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.ui.currencies.FewCurrencyEffect
import com.github.mustafaozhan.ccc.client.util.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ccc.common.model.Currency
import kotlinx.coroutines.flow.collect
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentCurrenciesBinding
import mustafaozhan.github.com.mycurrencies.databinding.ItemCurrenciesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrenciesFragment : BaseDBFragment<FragmentCurrenciesBinding>() {

    companion object {
        internal const val SPAN_PORTRAIT = 1
        internal const val SPAN_LANDSCAPE = 3
    }

    private val currenciesViewModel: CurrenciesViewModel by viewModel()

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
        initView()
        observeEffect()
    }

    override fun onResume() {
        super.onResume()
        binding.adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_currencies),
            currenciesViewModel.isRewardExpired()
        )
        currenciesViewModel.hideSelectionVisibility()
        currenciesViewModel.filterList("")
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

        lifecycleScope.launchWhenStarted {
            currenciesViewModel.state.currencyList.collect {
                currenciesAdapter.submitList(it)
            }
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        currenciesViewModel.effect.collect { viewEffect ->
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

    inner class CurrenciesAdapter(
        private val currenciesEvent: CurrenciesEvent
    ) : BaseDBRecyclerViewAdapter<Currency, ItemCurrenciesBinding>(
        CurrenciesAdapter(currenciesEvent).CurrenciesDiffer()
    ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RatesDBViewHolder(
            ItemCurrenciesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: BaseDBViewHolder<Currency, ItemCurrenciesBinding>,
            position: Int
        ) {
            holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.fall_down
                )
            )
            super.onBindViewHolder(holder, position)
        }

        override fun onViewDetachedFromWindow(holder: BaseDBViewHolder<Currency, ItemCurrenciesBinding>) {
            super.onViewDetachedFromWindow(holder)
            holder.itemView.clearAnimation()
        }

        inner class RatesDBViewHolder(itemBinding: ItemCurrenciesBinding) :
            BaseDBViewHolder<Currency, ItemCurrenciesBinding>(itemBinding) {

            override fun onItemBind(item: Currency) = with(itemBinding) {
                this.item = item
                this.event = currenciesEvent
            }
        }

        inner class CurrenciesDiffer : DiffUtil.ItemCallback<Currency>() {
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
                oldItem.isActive == newItem.isActive
        }
    }
}

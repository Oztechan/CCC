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
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.Toast.show
import com.github.mustafaozhan.ccc.android.util.backgroundByName
import com.github.mustafaozhan.ccc.android.util.hideKeyboard
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setNavigationResult
import com.github.mustafaozhan.ccc.android.util.visibleIf
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

class CurrenciesFragment : BaseVBFragment<FragmentCurrenciesBinding>() {

    companion object {
        internal const val SPAN_PORTRAIT = 1
        internal const val SPAN_LANDSCAPE = 3
    }

    private val currenciesViewModel: CurrenciesViewModel by viewModel()

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
        currenciesAdapter = CurrenciesAdapter(currenciesViewModel.getEvent())
        setSpanByOrientation(resources.configuration.orientation)

        with(recyclerViewCurrencies) {
            setHasFixedSize(true)
            adapter = currenciesAdapter
        }

        btnDone.visibleIf(currenciesViewModel.isFirstRun())
        txtSelectCurrencies.visibleIf(currenciesViewModel.isFirstRun())
    }

    private fun observeStates() = with(currenciesViewModel.state) {
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
                    backButton.visibleIf(!currenciesViewModel.isFirstRun() || it)

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

    private fun setListeners() = with(binding) {
        btnDone.setOnClickListener { currenciesViewModel.getEvent().onDoneClick() }

        with(layoutCurrenciesToolbar) {
            backButton.setOnClickListener {
                currenciesViewModel.getEvent().onCloseClick()
            }
            btnSelectAll.setOnClickListener {
                currenciesViewModel.getEvent().updateAllCurrenciesState(true)
            }
            btnDeSelectAll.setOnClickListener {
                currenciesViewModel.getEvent().updateAllCurrenciesState(false)
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = false
                override fun onQueryTextChange(newText: String) =
                    currenciesViewModel.filterList(newText)
            })
        }
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
}

class CurrenciesAdapter(
    private val currenciesEvent: CurrenciesEvent
) : BaseVBRecyclerViewAdapter<Currency, ItemCurrenciesBinding>(CurrenciesDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = RatesVBViewHolder(
        ItemCurrenciesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: BaseVBViewHolder<Currency, ItemCurrenciesBinding>,
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

    override fun onViewDetachedFromWindow(holder: BaseVBViewHolder<Currency, ItemCurrenciesBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    inner class RatesVBViewHolder(itemBinding: ItemCurrenciesBinding) :
        BaseVBViewHolder<Currency, ItemCurrenciesBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            imgIcon.backgroundByName(item.name)
            txtSettingItem.text = item.getVariablesOneLine()
            checkBox.isChecked = item.isActive
            root.setOnClickListener { currenciesEvent.onItemClick(item) }
            root.setOnLongClickListener { currenciesEvent.onItemLongClick() }
        }
    }

    class CurrenciesDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.isActive == newItem.isActive
    }
}

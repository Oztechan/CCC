/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mustafaozhan.basemob.adapter.BaseVBRecyclerViewAdapter
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.ui.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.github.mustafaozhan.ccc.android.util.Toast.show
import com.github.mustafaozhan.ccc.android.util.hideKeyboard
import com.github.mustafaozhan.ccc.android.util.setAdaptiveBannerAd
import com.github.mustafaozhan.ccc.android.util.setBackgroundByName
import com.github.mustafaozhan.ccc.android.util.visibleIf
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.viewmodel.CurrenciesEffect
import com.github.mustafaozhan.ccc.client.viewmodel.CurrenciesEvent
import com.github.mustafaozhan.ccc.client.viewmodel.CurrenciesViewModel
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
        kermit.d { "CurrenciesFragment onViewCreated" }
        initViews()
        observeStates()
        observeEffect()
        setListeners()
    }

    private fun initViews() = with(binding) {
        adViewContainer.setAdaptiveBannerAd(
            getString(R.string.banner_ad_unit_id_currencies),
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

    private fun observeStates() = lifecycleScope.launchWhenStarted {
        currenciesViewModel.state.collect {
            with(it) {
                currenciesAdapter.submitList(currencyList)

                binding.loadingView.visibleIf(loading)

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
        }
    }

    private fun observeEffect() = lifecycleScope.launchWhenStarted {
        currenciesViewModel.effect.collect { viewEffect ->
            kermit.d { "CurrenciesFragment observeEffect ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                CurrenciesEffect.FewCurrency -> show(
                    requireContext(),
                    R.string.choose_at_least_two_currency
                )
                CurrenciesEffect.OpenCalculator -> {
                    navigate(
                        R.id.currenciesFragment,
                        CurrenciesFragmentDirections.actionCurrenciesFragmentToCalculatorFragment()
                    )
                    view?.run { hideKeyboard() }
                }
                CurrenciesEffect.Back -> {
                    getBaseActivity()?.onBackPressed()
                    view?.run { hideKeyboard() }
                }
                is CurrenciesEffect.ChangeBase -> findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    CHANGE_BASE_EVENT,
                    viewEffect.newBase
                )
            }
        }
    }

    private fun setListeners() = with(binding) {
        with(currenciesViewModel.event) {

            btnDone.setOnClickListener { onDoneClick() }

            with(layoutCurrenciesToolbar) {

                backButton.setOnClickListener { onCloseClick() }
                btnSelectAll.setOnClickListener { updateAllCurrenciesState(true) }
                btnDeSelectAll.setOnClickListener { updateAllCurrenciesState(false) }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String) = false
                    override fun onQueryTextChange(newText: String) =
                        currenciesViewModel.filterList(newText)
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        kermit.d { "CurrenciesFragment onResume" }
        currenciesViewModel.hideSelectionVisibility()
        currenciesViewModel.filterList("")
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
            imgIcon.setBackgroundByName(item.name)
            txtSettingItem.text = item.getVariablesOneLine()
            checkBox.isChecked = item.isActive
            root.setOnClickListener { currenciesEvent.onItemClick(item) }
            root.setOnLongClickListener { currenciesEvent.onItemLongClick() }
        }
    }

    class CurrenciesDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = true

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.isActive == newItem.isActive
    }
}

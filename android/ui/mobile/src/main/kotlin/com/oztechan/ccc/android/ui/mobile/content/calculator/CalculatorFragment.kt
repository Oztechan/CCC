/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnApplyWindowInsetsListener
import android.view.WindowInsets
import android.widget.Button
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.core.view.updatePadding
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.touchlab.kermit.Logger
import com.github.submob.basemob.fragment.BaseVBFragment
import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.ui.mobile.BuildConfig
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.databinding.FragmentCalculatorBinding
import com.oztechan.ccc.android.ui.mobile.util.buildBanner
import com.oztechan.ccc.android.ui.mobile.util.copyToClipBoard
import com.oztechan.ccc.android.ui.mobile.util.dataState
import com.oztechan.ccc.android.ui.mobile.util.destroyBanner
import com.oztechan.ccc.android.ui.mobile.util.getFromClipBoard
import com.oztechan.ccc.android.ui.mobile.util.setBackgroundByName
import com.oztechan.ccc.android.ui.mobile.util.showSnack
import com.oztechan.ccc.android.ui.mobile.util.visibleIf
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.core.res.getImageIdByName
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorEffect
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.oztechan.ccc.client.viewmodel.calculator.util.toValidList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class CalculatorFragment : BaseVBFragment<FragmentCalculatorBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val adManager: AdManager by inject()
    private val viewModel: CalculatorViewModel by viewModel()

    private val calculatorAdapter: CalculatorAdapter by lazy {
        CalculatorAdapter(viewModel.event)
    }

    override fun getViewBinding() = FragmentCalculatorBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "CalculatorFragment onViewCreated" }
        binding.initViews()
        binding.observeStates()
        binding.setListeners()
        observeEffects()
    }

    override fun onResume() {
        super.onResume()
        Logger.i { "CalculatorFragment onResume" }
        analyticsManager.trackScreen(ScreenName.Calculator)
    }

    override fun onDestroy() {
        Logger.i { "CalculatorFragment onDestroy" }
        super.onDestroy()
    }

    override fun onDestroyView() {
        Logger.i { "CalculatorFragment onDestroyView" }
        binding.adViewContainer.destroyBanner()
        binding.recyclerViewMain.adapter = null
        super.onDestroyView()
    }

    private fun FragmentCalculatorBinding.initViews() {
        val initialPaddings = InitialSpacings(24,0,0,0)
        toolbarContainer?.doOnApplyWindowInsets {
            setPaddingVerticalAndSystemWindowInsets(
                it,
                skipBottom = true,
                initialPaddings = initialPaddings
            )
        }

        adViewContainer.rootView.doOnApplyWindowInsets {
            setPaddingVerticalAndSystemWindowInsets(it, skipTop = true)
        }
        adViewContainer.buildBanner(
            adManager = adManager,
            adId = if (BuildConfig.DEBUG) {
                getString(R.string.banner_ad_unit_id_calculator_debug)
            } else {
                getString(R.string.banner_ad_unit_id_calculator_release)
            },
            shouldShowAd = viewModel.state.value.isBannerAdVisible
        )
        recyclerViewMain.adapter = calculatorAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentCalculatorBinding.observeStates() = viewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                calculatorAdapter.submitList(currencyList.toValidList(viewModel.state.value.base))

                txtInput.text = input
                with(layoutBar) {
                    ivBase.setBackgroundByName(base)
                    txtBase.text = if (base.isEmpty()) base else "  $base"
                    txtOutput.text = if (output.isNotEmpty()) " = $output" else ""
                    txtSymbol.text = " $symbol"
                }

                loadingView.visibleIf(loading, true)
                txtAppStatus.dataState(conversionState)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = viewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "CalculatorFragment observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                CalculatorEffect.Error -> view?.showSnack(R.string.error_text_unknown)
                CalculatorEffect.FewCurrency -> view?.showSnack(
                    R.string.choose_at_least_two_currency,
                    R.string.select
                ) {
                    navigate(
                        R.id.calculatorFragment,
                        CalculatorFragmentDirections.actionCalculatorFragmentToCurrenciesFragment()
                    )
                }

                CalculatorEffect.TooBigInput -> view?.showSnack(R.string.text_too_big_input)
                CalculatorEffect.TooBigOutput -> view?.showSnack(R.string.text_too_big_output)
                CalculatorEffect.OpenBar -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToSelectCurrencyBottomSheet()
                )

                CalculatorEffect.OpenSettings -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment()
                )

                CalculatorEffect.ShowPasteRequest -> view?.let {
                    it.showSnack(
                        text = R.string.text_paste_request,
                        actionText = R.string.text_paste
                    ) {
                        viewModel.onPasteToInput(it.context.getFromClipBoard())
                    }
                }

                is CalculatorEffect.CopyToClipboard -> view?.copyToClipBoard(viewEffect.amount)
                is CalculatorEffect.ShowConversion -> view?.showSnack(
                    viewEffect.text,
                    icon = viewEffect.code.getImageIdByName()
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun FragmentCalculatorBinding.setListeners() = with(viewModel.event) {
        btnSettings.setOnClickListener { onSettingsClicked() }
        layoutBar.root.setOnClickListener { onBarClick() }

        layoutBar.txtOutput.setOnClickListener { onBarClick() }
        layoutBar.txtOutput.setOnLongClickListener {
            onOutputLongClick()
            true
        }

        txtInput.setOnLongClickListener {
            onInputLongClick()
            true
        }

        with(layoutKeyboard) {
            one.setKeyboardListener()
            two.setKeyboardListener()
            three.setKeyboardListener()
            four.setKeyboardListener()
            five.setKeyboardListener()
            six.setKeyboardListener()
            seven.setKeyboardListener()
            eight.setKeyboardListener()
            nine.setKeyboardListener()
            zero.setKeyboardListener()
            tripleZero.setKeyboardListener()
            multiply.setKeyboardListener()
            divide.setKeyboardListener()
            minus.setKeyboardListener()
            plus.setKeyboardListener()
            percent.setKeyboardListener()
            dot.setKeyboardListener()
            openParentheses.setKeyboardListener()
            closeParentheses.setKeyboardListener()
            ac.setKeyboardListener()
            del.setKeyboardListener()
        }

        findNavController().addOnDestinationChangedListener { _, navDestination, _ ->
            if (navDestination.id == R.id.calculatorFragment) {
                onSheetDismissed()
            }
        }
    }

    private fun Button.setKeyboardListener() = setOnClickListener {
        viewModel.event.onKeyPress(text.toString())
    }
}

/**
 * Sets an [OnApplyWindowInsetsListener] which proxies to the given lambda.
 *
 * Based on: https://deploy-preview-9--festive-chandrasekhar-4b30b6.netlify.app/2019/04/12/insets-listeners-to-layouts/
 */
fun View.doOnApplyWindowInsets(apply: View.(insets: WindowInsets) -> Unit) {
    setOnApplyWindowInsetsListener { view, insets ->
        view.apply(insets)
        insets
    }
    doOnAttach { requestApplyInsets() }
}

fun View.setPaddingVerticalAndSystemWindowInsets(
    insets: WindowInsets,
    skipTop: Boolean = false,
    skipBottom: Boolean = false,
    initialPaddings: InitialSpacings? = null,
) {
    val insetsCompat = insets.getSystemWindowInsetsCompat()
    updatePadding(
        top = if (skipTop) paddingTop else insetsCompat.top + (initialPaddings?.top ?: 0),
        bottom = if (skipBottom) paddingBottom else insetsCompat.bottom + (initialPaddings?.bottom
            ?: 0),
    )
}

fun WindowInsets.getSystemWindowInsetsCompat() =
    WindowInsetsCompat.toWindowInsetsCompat(this)
        .getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())

data class InitialSpacings(val top: Int, val end: Int, val bottom: Int, val start: Int)

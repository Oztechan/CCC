/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.calculator

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.Param
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.constants.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.client.core.shared.util.MAXIMUM_FLOATING_POINT
import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.nowAsDateString
import com.oztechan.ccc.client.core.shared.util.toStandardDigits
import com.oztechan.ccc.client.core.shared.util.toSupportedCharacters
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.CHAR_DOT
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_INPUT
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_OUTPUT
import com.oztechan.ccc.client.viewmodel.calculator.model.ConversionState
import com.oztechan.ccc.client.viewmodel.calculator.util.calculateRate
import com.oztechan.ccc.client.viewmodel.calculator.util.getConversionStringFromBase
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.datasource.conversion.ConversionDataSource
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val calculationStorage: CalculationStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val conversionDataSource: ConversionDataSource,
    adControlRepository: AdControlRepository,
    private val analyticsManager: AnalyticsManager
) : SEEDViewModel<CalculatorState, CalculatorEffect, CalculatorEvent, CalculatorData>(
    initialState = CalculatorState(isBannerAdVisible = adControlRepository.shouldShowBannerAd()),
    initialData = CalculatorData()
),
    CalculatorEvent {
    // region SEED
    override val event = this as CalculatorEvent
    // endregion

    init {
        currencyDataSource.getActiveCurrenciesFlow()
            .onStart {
                val activeCurrencies = currencyDataSource.getActiveCurrencies()
                setState {
                    copy(
                        currencyList = activeCurrencies,
                        base = calculationStorage.currentBase,
                        input = calculationStorage.lastInput,
                        loading = true
                    )
                }
                updateConversion()
                observeBase()
                observeInput()
            }
            .onEach {
                Logger.d { "CalculatorViewModel currencyList changed: ${it.joinToString(",")}" }
                setState { copy(currencyList = it) }

                analyticsManager.setUserProperty(UserProperty.CurrencyCount(it.count().toString()))
            }
            .launchIn(viewModelScope)
    }

    private fun observeBase() = state.map { it.base }
        .distinctUntilChanged()
        .onEach {
            Logger.d { "CalculatorViewModel observeBase $it" }
            currentBaseChanged(it, true)
        }
        .launchIn(viewModelScope)

    private fun observeInput() = state.map { it.input }
        .distinctUntilChanged()
        .onEach {
            Logger.d { "CalculatorViewModel observeInput $it" }
            calculationStorage.lastInput = it
            calculateOutput(it)
        }
        .launchIn(viewModelScope)

    private fun updateConversion() {
        setState { copy(loading = true) }

        data.conversion?.let {
            calculateConversions(it, ConversionState.Cached(it.date))
        } ?: viewModelScope.launch {
            runCatching { backendApiService.getConversion(calculationStorage.currentBase) }
                .onFailure(::updateConversionFailed)
                .onSuccess(::updateConversionSuccess)
        }
    }

    private fun updateConversionSuccess(conversion: Conversion) =
        conversion.copy(date = nowAsDateString())
            .let {
                data.conversion = it
                calculateConversions(it, ConversionState.Online(it.date))

                viewModelScope.launch {
                    conversionDataSource.insertConversion(it)
                }
            }

    private fun updateConversionFailed(t: Throwable) = viewModelScope.launchIgnored {
        Logger.w(t) { "CalculatorViewModel updateConversionFailed" }
        conversionDataSource.getConversionByBase(
            calculationStorage.currentBase
        )?.let {
            calculateConversions(it, ConversionState.Offline(it.date))
        } ?: run {
            Logger.w { "No offline conversion found in the DB" }

            setEffect { CalculatorEffect.Error }

            calculateConversions(null, ConversionState.Error)
        }
    }

    private fun calculateConversions(conversion: Conversion?, conversionState: ConversionState) =
        setState {
            copy(
                currencyList = state.value.currencyList.onEach {
                    it.rate = conversion.calculateRate(it.code, state.value.output)
                        .getFormatted(calculationStorage.precision)
                        .toStandardDigits()
                },
                conversionState = conversionState,
                loading = false
            )
        }

    private fun calculateOutput(input: String) = viewModelScope.launch {
        val output = data.parser
            .calculate(input.toSupportedCharacters(), MAXIMUM_FLOATING_POINT)
            .mapTo { if (it.isFinite()) it.getFormatted(calculationStorage.precision) else "" }

        setState { copy(output = output) }

        when {
            input.length > MAXIMUM_INPUT -> {
                setEffect { CalculatorEffect.TooBigInput }
                setState { copy(input = input.dropLast(1)) }
            }

            output.length > MAXIMUM_OUTPUT -> {
                setEffect { CalculatorEffect.TooBigOutput }
                setState { copy(input = input.dropLast(1)) }
            }

            state.value.currencyList.size < MINIMUM_ACTIVE_CURRENCY -> {
                setEffect { CalculatorEffect.FewCurrency }
                setState { copy(loading = false) }
            }

            else -> updateConversion()
        }
    }

    private fun currentBaseChanged(newBase: String, shouldTrack: Boolean = false) =
        viewModelScope.launchIgnored {
            data.conversion = null
            calculationStorage.currentBase = newBase
            val symbol = currencyDataSource.getCurrencyByCode(newBase)?.symbol.orEmpty()
            setState {
                copy(
                    base = newBase,
                    input = input,
                    symbol = symbol
                )
            }

            if (shouldTrack) {
                analyticsManager.trackEvent(Event.BaseChange(Param.Base(newBase)))
                analyticsManager.setUserProperty(UserProperty.BaseCurrency(newBase))
            }
        }

    // region Event
    override fun onKeyPress(key: String) {
        Logger.d { "CalculatorViewModel onKeyPress $key" }

        when (key) {
            KEY_AC -> setState { copy(input = "") }
            KEY_DEL ->
                state.value.input
                    .whetherNot { it.isEmpty() }
                    ?.apply {
                        setState { copy(input = substring(0, length - 1)) }
                    }

            else -> setState { copy(input = state.value.input + key) }
        }
    }

    override fun onItemClick(currency: Currency) = with(currency) {
        Logger.d { "CalculatorViewModel onItemClick ${currency.code}" }

        val newInput = rate.toSupportedCharacters().let {
            if (it.last() == CHAR_DOT) {
                it.dropLast(1)
            } else {
                it
            }
        }

        setState {
            copy(
                base = code,
                input = newInput
            )
        }
    }

    override fun onItemImageLongClick(currency: Currency) {
        Logger.d { "CalculatorViewModel onItemImageLongClick ${currency.code}" }

        analyticsManager.trackEvent(Event.ShowConversion(Param.Base(currency.code)))

        setEffect {
            CalculatorEffect.ShowConversion(
                currency.getConversionStringFromBase(
                    calculationStorage.currentBase,
                    data.conversion
                ),
                currency.code
            )
        }
    }

    override fun onItemAmountLongClick(amount: String) {
        Logger.d { "CalculatorViewModel onItemAmountLongClick $amount" }

        analyticsManager.trackEvent(Event.CopyClipboard)

        setEffect { CalculatorEffect.CopyToClipboard(amount) }
    }

    override fun onOutputLongClick() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onOutputLongClick" }

        analyticsManager.trackEvent(Event.CopyClipboard)

        setEffect { CalculatorEffect.CopyToClipboard(state.value.output) }
    }

    override fun onInputLongClick() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onInputLongClick" }
        setEffect { CalculatorEffect.ShowPasteRequest }
    }

    override fun onPasteToInput(text: String) {
        Logger.d { "CalculatorViewModel onPasteToInput $text" }

        analyticsManager.trackEvent(Event.PasteFromClipboard)

        setState { copy(input = text.toSupportedCharacters()) }
    }

    override fun onBarClick() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onBarClick" }
        setEffect { CalculatorEffect.OpenBar }
    }

    override fun onSettingsClicked() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onSettingsClicked" }
        setEffect { CalculatorEffect.OpenSettings }
    }

    override fun onBaseChange(base: String) {
        Logger.d { "CalculatorViewModel onBaseChange $base" }
        currentBaseChanged(base)
        calculateOutput(state.value.input)
    }
    // endregion
}

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
import com.oztechan.ccc.client.core.viewmodel.BaseSEEDViewModel
import com.oztechan.ccc.client.core.viewmodel.util.launchIgnored
import com.oztechan.ccc.client.core.viewmodel.util.update
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : BaseSEEDViewModel<CalculatorState, CalculatorEffect, CalculatorEvent, CalculatorData>(),
    CalculatorEvent {
    // region SEED
    private val _state =
        MutableStateFlow(CalculatorState(isBannerAdVisible = adControlRepository.shouldShowBannerAd()))
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CalculatorEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as CalculatorEvent

    override val data = CalculatorData()
    // endregion

    init {
        currencyDataSource.getActiveCurrenciesFlow()
            .onStart {
                _state.update {
                    copy(
                        currencyList = currencyDataSource.getActiveCurrencies(),
                        base = calculationStorage.getBase(),
                        input = calculationStorage.getLastInput(),
                        loading = true
                    )
                }
                updateConversion()
                observeBase()
                observeInput()
            }
            .onEach {
                Logger.d { "CalculatorViewModel currencyList changed: ${it.joinToString(",")}" }
                _state.update { copy(currencyList = it) }

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
            calculationStorage.setLastInput(it)
            calculateOutput(it)
        }
        .launchIn(viewModelScope)

    private suspend fun updateConversion() {
        _state.update { copy(loading = true) }

        data.conversion?.let {
            calculateConversions(it, ConversionState.Cached(it.date))
        } ?: viewModelScope.launch {
            runCatching { backendApiService.getConversion(calculationStorage.getBase()) }
                .onFailure(::updateConversionFailed)
                .onSuccess(::updateConversionSuccess)
        }
    }

    private fun updateConversionSuccess(conversion: Conversion) =
        conversion.copy(date = nowAsDateString())
            .let {
                data.conversion = it
                viewModelScope.launch {
                    calculateConversions(it, ConversionState.Online(it.date))
                    conversionDataSource.insertConversion(it)
                }
            }

    private fun updateConversionFailed(t: Throwable) = viewModelScope.launchIgnored {
        Logger.w(t) { "CalculatorViewModel updateConversionFailed" }
        conversionDataSource.getConversionByBase(
            calculationStorage.getBase()
        )?.let {
            calculateConversions(it, ConversionState.Offline(it.date))
        } ?: run {
            Logger.w { "No offline conversion found in the DB" }

            _effect.emit(CalculatorEffect.Error)

            calculateConversions(null, ConversionState.Error)
        }
    }

    private suspend fun calculateConversions(
        conversion: Conversion?,
        conversionState: ConversionState
    ) = _state.update {
        copy(
            currencyList = _state.value.currencyList.onEach {
                it.rate = conversion.calculateRate(it.code, _state.value.output)
                    .getFormatted(calculationStorage.getPrecision())
                    .toStandardDigits()
            },
            conversionState = conversionState,
            loading = false
        )
    }

    private fun calculateOutput(input: String) = viewModelScope.launch {
        val output = data.parser
            .calculate(input.toSupportedCharacters(), MAXIMUM_FLOATING_POINT)
            .mapTo { if (isFinite()) getFormatted(calculationStorage.getPrecision()) else "" }

        _state.update { copy(output = output) }

        when {
            input.length > MAXIMUM_INPUT -> {
                _effect.emit(CalculatorEffect.TooBigInput)
                _state.update { copy(input = input.dropLast(1)) }
            }

            output.length > MAXIMUM_OUTPUT -> {
                _effect.emit(CalculatorEffect.TooBigOutput)
                _state.update { copy(input = input.dropLast(1)) }
            }

            state.value.currencyList.size < MINIMUM_ACTIVE_CURRENCY -> {
                _effect.emit(CalculatorEffect.FewCurrency)
                _state.update { copy(loading = false) }
            }

            else -> updateConversion()
        }
    }

    private fun currentBaseChanged(newBase: String, shouldTrack: Boolean = false) =
        viewModelScope.launchIgnored {
            data.conversion = null
            calculationStorage.setBase(newBase)
            _state.update {
                copy(
                    base = newBase,
                input = input,
                    symbol = currencyDataSource.getCurrencyByCode(newBase)?.symbol.orEmpty()
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
            KEY_AC -> _state.update { copy(input = "") }
            KEY_DEL ->
                state.value.input
                    .whetherNot { isEmpty() }
                    ?.apply {
                        _state.update { copy(input = substring(0, length - 1)) }
                    }

            else -> _state.update { copy(input = state.value.input + key) }
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

        _state.update {
            copy(
                base = code,
                input = newInput
            )
        }
    }

    override fun onItemImageLongClick(currency: Currency) {
        Logger.d { "CalculatorViewModel onItemImageLongClick ${currency.code}" }

        analyticsManager.trackEvent(Event.ShowConversion(Param.Base(currency.code)))

        viewModelScope.launch {
            _effect.emit(
                CalculatorEffect.ShowConversion(
                    currency.getConversionStringFromBase(
                        calculationStorage.getBase(),
                        data.conversion
                    ),
                    currency.code
                )
            )
        }
    }

    override fun onItemAmountLongClick(amount: String) {
        Logger.d { "CalculatorViewModel onItemAmountLongClick $amount" }

        analyticsManager.trackEvent(Event.CopyClipboard)

        viewModelScope.launch {
            _effect.emit(CalculatorEffect.CopyToClipboard(amount))
        }
    }

    override fun onOutputLongClick() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onOutputLongClick" }

        analyticsManager.trackEvent(Event.CopyClipboard)

        _effect.emit(CalculatorEffect.CopyToClipboard(state.value.output))
    }

    override fun onInputLongClick() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onInputLongClick" }
        _effect.emit(CalculatorEffect.ShowPasteRequest)
    }

    override fun onPasteToInput(text: String) {
        Logger.d { "CalculatorViewModel onPasteToInput $text" }

        analyticsManager.trackEvent(Event.PasteFromClipboard)

        _state.update { copy(input = text.toSupportedCharacters()) }
    }

    override fun onBarClick() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onBarClick" }
        _effect.emit(CalculatorEffect.OpenBar)
    }

    override fun onSettingsClicked() = viewModelScope.launchIgnored {
        Logger.d { "CalculatorViewModel onSettingsClicked" }
        _effect.emit(CalculatorEffect.OpenSettings)
    }

    override fun onBaseChange(base: String) {
        Logger.d { "CalculatorViewModel onBaseChange $base" }
        currentBaseChanged(base)
        calculateOutput(_state.value.input)
    }
    // endregion
}

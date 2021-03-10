/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.model.toModel
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.update
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.parsermob.ParserMob
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseSEEDViewModel(), CalculatorEvent {

    companion object {
        private const val MAXIMUM_INPUT = 18
        private const val CHAR_DOT = '.'
        private const val PRECISION = 9
        const val KEY_DEL = "DEL"
        const val KEY_AC = "AC"
    }

    // region SEED
    private val _state = MutableStateFlow(CalculatorState())
    override val state: StateFlow<CalculatorState> = _state

    private val _effect = Channel<CalculatorEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as CalculatorEvent

    override val data = CalculatorData()
    // endregion

    init {
        kermit.d { "CalculatorViewModel init" }
        _state.update(base = settingsRepository.currentBase, input = "")

        clientScope.launch {
            state.map { it.base }
                .distinctUntilChanged()
                .collect { currentBaseChanged(it) }
        }

        clientScope.launch {
            state.map { it.input }
                .distinctUntilChanged()
                .collect { calculateOutput(it) }
        }

        clientScope.launch {
            currencyDao.collectActiveCurrencies()
                .mapToModel()
                .collect { _state.update(currencyList = it) }
        }
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
        _state.update(dataState = DataState.Cached(rates.date))
    } ?: clientScope.launch {
        apiRepository
            .getRatesByBaseViaBackend(settingsRepository.currentBase)
            .execute(
                ::rateDownloadSuccess,
                ::rateDownloadFail
            )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) = currencyResponse
        .toRates().let {
            data.rates = it
            calculateConversions(it)
            _state.update(dataState = DataState.Online(it.date))
            offlineRatesDao.insertOfflineRates(it)
        }

    private fun rateDownloadFail(t: Throwable) {
        kermit.w(t) { "rate download failed." }

        offlineRatesDao.getOfflineRatesByBase(
            settingsRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _state.update(dataState = DataState.Offline(offlineRates.date))
        } ?: clientScope.launch {
            kermit.w(t) { "no offline rate found" }
            state.value.currencyList.size
                .whether { it > 1 }
                ?.let { _effect.send(CalculatorEffect.Error) }
            _state.update(dataState = DataState.Error)
        }
    }

    private fun calculateOutput(input: String) = clientScope.launch {
        _state.update(loading = true)
        data.parser
            .calculate(input.toSupportedCharacters(), PRECISION)
            .mapTo { if (isFinite()) getFormatted() else "" }
            .whether { length <= MAXIMUM_INPUT }
            ?.let { output ->
                _state.update(output = output)
                state.value.currencyList.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.value.input.isEmpty() }
                    ?.let { _effect.send(CalculatorEffect.FewCurrency) }
                    ?: run { getRates() }
            } ?: run {
            _effect.send(CalculatorEffect.MaximumInput)
            _state.update(
                input = input.dropLast(1),
                loading = false
            )
        }
    }

    private fun calculateConversions(rates: Rates?) = _state.update(
        currencyList = _state.value.currencyList.onEach {
            it.rate = rates.calculateResult(it.name, _state.value.output)
        },
        loading = false
    )

    private fun currentBaseChanged(newBase: String) {
        data.rates = null
        settingsRepository.currentBase = newBase
        calculateOutput(_state.value.input)
        _state.update(
            base = newBase,
            input = _state.value.input,
            symbol = currencyDao.getCurrencyByName(newBase)?.toModel()?.symbol ?: ""
        )
    }

    fun isRewardExpired() = settingsRepository.adFreeEndDate.isRewardExpired()

    override fun onCleared() {
        kermit.d { "CalculatorViewModel onCleared" }
        super.onCleared()
    }

    // region Event
    override fun onKeyPress(key: String) {
        kermit.d { "CalculatorViewModel onKeyPress $key" }
        when (key) {
            KEY_AC -> _state.update(input = "", output = "")
            KEY_DEL -> state.value.input
                .whetherNot { isEmpty() }
                ?.apply {
                    _state.update(input = substring(0, length - 1))
                }
            else -> _state.update(input = if (key.isEmpty()) "" else state.value.input + key)
        }
    }

    override fun onItemClick(currency: Currency, conversion: String) {
        kermit.d { "CalculatorViewModel onItemClick ${currency.name} $conversion" }
        var finalResult = conversion

        while (finalResult.length > MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _state.update(base = currency.name, input = finalResult)
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        kermit.d { "CalculatorViewModel onItemLongClick ${currency.name}" }
        clientScope.launch {
            _effect.send(
                CalculatorEffect.ShowRate(
                    currency.getCurrencyConversionByRate(
                        settingsRepository.currentBase,
                        data.rates
                    ),
                    currency.name
                )
            )
        }
        return true
    }

    override fun onBarClick() = clientScope.launch {
        kermit.d { "CalculatorViewModel onBarClick" }
        _effect.send(CalculatorEffect.OpenBar)
    }.toUnit()

    override fun onSpinnerItemSelected(base: String) {
        kermit.d { "CalculatorViewModel onSpinnerItemSelected $base" }
        _state.update(base = base)
    }

    override fun onSettingsClicked() = clientScope.launch {
        kermit.d { "CalculatorViewModel onSettingsClicked" }
        _effect.send(CalculatorEffect.OpenSettings)
    }.toUnit()

    override fun onBaseChange(base: String) = currentBaseChanged(base)
    // endregion
}

// region SEED
data class CalculatorState(
    val input: String = "",
    val base: String = "",
    val currencyList: List<Currency> = listOf(),
    val output: String = "",
    val symbol: String = "",
    val loading: Boolean = true,
    val dataState: DataState = DataState.Error,
) : BaseState() {
    // for ios
    constructor() : this("", "", listOf(), "", "", true, DataState.Error)
}

interface CalculatorEvent : BaseEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency, conversion: String)
    fun onItemLongClick(currency: Currency): Boolean
    fun onBarClick()
    fun onSpinnerItemSelected(base: String)
    fun onSettingsClicked()
    fun onBaseChange(base: String)
}

sealed class CalculatorEffect : BaseEffect() {
    object Error : CalculatorEffect()
    object FewCurrency : CalculatorEffect()
    object OpenBar : CalculatorEffect()
    object MaximumInput : CalculatorEffect()
    object OpenSettings : CalculatorEffect()
    data class ShowRate(val text: String, val name: String) : CalculatorEffect()
}

data class CalculatorData(
    var parser: ParserMob = ParserMob(),
    var rates: Rates? = null
) : BaseData()
// endregion

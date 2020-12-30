/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.calculator

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.client.model.mapToModel
import com.github.mustafaozhan.ccc.client.model.toModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorState.Companion.update
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyDao: CurrencyDao,
    private val offlineRatesDao: OfflineRatesDao
) : BaseViewModel(), CalculatorEvent {

    companion object {
        private const val MAXIMUM_INPUT = 18
        private const val CHAR_DOT = '.'
        const val KEY_DEL = "DEL"
        const val KEY_AC = "AC"
    }

    // region SEED
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    private val _effect = BroadcastChannel<CalculatorEffect>(Channel.BUFFERED)
    val effect = _effect.asFlow()

    val data = CalculatorData()

    fun getEvent() = this as CalculatorEvent
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
                .collect { input ->
                    _state.update(loading = true)
                    calculateOutput(input)
                }
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
                ?.let { _effect.send(ErrorEffect) }
            _state.update(dataState = DataState.Error)
        }
    }

    private fun calculateOutput(input: String) = clientScope.launch {
        data.calculator
            .calculate(input.toSupportedCharacters())
            .mapTo { if (isFinite()) getFormatted() else "" }
            .whether { length <= MAXIMUM_INPUT }
            ?.let { output ->
                _state.update(output = output)
                state.value.currencyList.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.value.input.isEmpty() }
                    ?.let { _effect.send(FewCurrencyEffect) }
                    ?: run { getRates() }
            } ?: run {
            _effect.send(MaximumInputEffect)
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
        _state.update(
            input = _state.value.input,
            symbol = currencyDao.getCurrencyByName(newBase)?.toModel()?.symbol ?: ""
        )
    }

    fun verifyCurrentBase(it: String) {
        _state.update(base = it)
    }

    fun getCurrentBase() = settingsRepository.currentBase

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

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
                ShowRateEffect(
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
        _effect.send(OpenBarEffect)
    }.toUnit()

    override fun onSpinnerItemSelected(base: String) {
        kermit.d { "CalculatorViewModel onSpinnerItemSelected $base" }
        _state.update(base = base)
    }

    override fun onSettingsClicked() = clientScope.launch {
        kermit.d { "CalculatorViewModel onSettingsClicked" }
        _effect.send(OpenSettingsEffect)
    }.toUnit()
    // endregion
}

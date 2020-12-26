/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.ui.calculator

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.client.util.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.removeUnUsedCurrencies
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.db.CurrencyDao
import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
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
    private val _state = MutableCalculatorState()
    val state = CalculatorState(_state)

    private val _effect = BroadcastChannel<CalculatorEffect>(Channel.BUFFERED)
    val effect = _effect.asFlow()

    val data = CalculatorData()

    fun getEvent() = this as CalculatorEvent
    // endregion

    init {
        with(_state) {
            _base.value = settingsRepository.currentBase
            _input.value = ""

            clientScope.launch {
                _base.collect {
                    currentBaseChanged(it)
                }
            }

            clientScope.launch {
                _input.collect { input ->
                    _loading.value = true
                    calculateOutput(input)
                }
            }

            clientScope.launch {
                currencyDao.collectActiveCurrencies()
                    .map { it.removeUnUsedCurrencies() }
                    .collect { _currencyList.value = it }
            }
        }
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
        _state._dataState.value = DataState.Cached(rates.date)
    } ?: clientScope.launch {
        apiRepository
            .getRatesByBaseViaBackend(settingsRepository.currentBase)
            .execute(
                ::rateDownloadSuccess,
                ::rateDownloadFail
            )
    }

    private fun rateDownloadSuccess(currencyResponse: CurrencyResponse) = clientScope.launch {
        currencyResponse.toRates().let {
            data.rates = it
            calculateConversions(it)
            _state._dataState.value = DataState.Online(it.date)
            offlineRatesDao.insertOfflineRates(it)
        }
    }.toUnit()

    private fun rateDownloadFail(t: Throwable) = clientScope.launch {
        kermit.w(t) { "rate download failed." }

        offlineRatesDao.getOfflineRatesByBase(
            settingsRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _state._dataState.value = DataState.Offline(offlineRates.date)
        } ?: run {
            kermit.w(t) { "no offline rate found" }
            state.currencyList.value.size
                .whether { it > 1 }
                ?.let { _effect.send(ErrorEffect) }
            _state._dataState.value = DataState.Error
        }
    }.toUnit()

    private fun calculateOutput(input: String) = clientScope.launch {
        data.calculator
            .calculate(input.toSupportedCharacters())
            .mapTo { if (isFinite()) getFormatted() else "" }
            .whether { length <= MAXIMUM_INPUT }
            ?.let { output ->
                _state._output.value = output
                state.currencyList.value.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.input.value.isEmpty() }
                    ?.let { _effect.send(FewCurrencyEffect) }
                    ?: run { getRates() }
            } ?: run {
            _effect.send(MaximumInputEffect)
            _state._input.value = input.dropLast(1)
            _state._loading.value = false
        }
    }

    private fun calculateConversions(rates: Rates?) = with(_state) {
        _currencyList.value.onEach {
            it.rate = rates.calculateResult(it.name, _output.value)
        }.let {
            _currencyList.value = mutableListOf()
            _currencyList.value = it
        }
        _loading.value = false
    }

    private fun currentBaseChanged(newBase: String) {
        data.rates = null
        settingsRepository.currentBase = newBase

        _state._input.value = _state._input.value

        clientScope.launch {
            _state._symbol.value = currencyDao.getCurrencyByName(newBase)?.symbol ?: ""
        }
    }

    fun verifyCurrentBase(it: String) {
        clientScope.launch {
            _state._base.emit(it)
        }
    }

    fun getCurrentBase() = settingsRepository.currentBase

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    // region Event
    override fun onKeyPress(key: String) {
        when (key) {
            KEY_AC -> {
                _state._input.value = ""
                _state._output.value = ""
            }
            KEY_DEL -> state.input.value
                .whetherNot { isEmpty() }
                ?.apply {
                    _state._input.value = substring(0, length - 1)
                }
            else -> _state._input.value = if (key.isEmpty()) "" else state.input.value + key
        }
    }

    override fun onItemClick(currency: Currency, conversion: String) = with(_state) {
        var finalResult = conversion

        while (finalResult.length > MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _base.value = currency.name
        _input.value = finalResult
    }

    override fun onItemLongClick(currency: Currency): Boolean {
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
        _effect.send(OpenBarEffect)
    }.toUnit()

    override fun onSpinnerItemSelected(base: String) {
        _state._base.value = base
    }

    override fun onSettingsClicked() = clientScope.launch {
        _effect.send(OpenSettingsEffect)
    }.toUnit()
    // endregion
}

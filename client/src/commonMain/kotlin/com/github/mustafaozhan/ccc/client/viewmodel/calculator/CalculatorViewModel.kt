/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel.calculator

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.mapper.toRates
import com.github.mustafaozhan.ccc.client.mapper.toTodayResponse
import com.github.mustafaozhan.ccc.client.mapper.toUIModelList
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.RateState
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.launchIgnored
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.CHAR_DOT
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_INPUT
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_OUTPUT
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorData.Companion.PRECISION
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.github.mustafaozhan.ccc.common.api.repo.ApiRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository,
    private val configManager: ConfigManager
) : BaseSEEDViewModel(), CalculatorEvent {
    // region SEED
    private val _state = MutableStateFlow(CalculatorState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CalculatorEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as CalculatorEvent

    override val data = CalculatorData()
    // endregion

    init {
        _state.update(base = settingsRepository.currentBase, input = "")

        state.map { it.base }
            .distinctUntilChanged()
            .onEach {
                Logger.d { "CalculatorViewModel base changed $it" }
                currentBaseChanged(it)
            }
            .launchIn(clientScope)

        state.map { it.input }
            .distinctUntilChanged()
            .onEach {
                Logger.d { "CalculatorViewModel input changed $it" }
                calculateOutput(it)
            }
            .launchIn(clientScope)

        currencyRepository.collectActiveCurrencies()
            .onEach {
                Logger.d { "CalculatorViewModel currencyList changed\n${it.joinToString("\n")}" }
                _state.update(currencyList = it.toUIModelList())
            }
            .launchIn(clientScope)
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
        _state.update(rateState = RateState.Cached(rates.date))
    } ?: clientScope.launch {
        apiRepository
            .getRatesByBackend(settingsRepository.currentBase)
            .execute(::getRatesSuccess, ::getRatesFailed)
    }

    private fun getRatesSuccess(currencyResponse: CurrencyResponse) = currencyResponse
        .toRates().let {
            data.rates = it
            calculateConversions(it)
            _state.update(rateState = RateState.Online(it.date))
        }.also {
            offlineRatesRepository.insertOfflineRates(currencyResponse.toTodayResponse())
        }

    private fun getRatesFailed(t: Throwable) {
        Logger.w(t) { "CalculatorViewModel getRatesFailed" }
        offlineRatesRepository.getOfflineRatesByBase(
            settingsRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _state.update(rateState = RateState.Offline(offlineRates.date))
        } ?: clientScope.launch {
            Logger.w(Exception("No offline rates")) { this@CalculatorViewModel::class.simpleName.toString() }

            state.value.currencyList.size
                .whether { it > 1 }
                ?.let { _effect.emit(CalculatorEffect.Error) }
            _state.update(rateState = RateState.Error)
        }
    }

    private fun calculateOutput(input: String) = clientScope.launch {
        _state.update(loading = true)
        data.parser
            .calculate(input.toSupportedCharacters(), PRECISION)
            .mapTo { if (isFinite()) getFormatted() else "" }
            .whether(
                { output -> output.length <= MAXIMUM_OUTPUT },
                { input.length <= MAXIMUM_INPUT }
            )?.let { output ->
                _state.update(output = output)
                state.value.currencyList.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.value.input.isEmpty() }
                    ?.let { _effect.emit(CalculatorEffect.FewCurrency) }
                    ?: run { getRates() }
            } ?: run {
            _effect.emit(CalculatorEffect.MaximumInput)
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
            base = newBase,
            input = _state.value.input,
            symbol = currencyRepository.getCurrencyByName(newBase)?.symbol ?: ""
        )
    }

    fun shouldShowBannerAd() = !settingsRepository.firstRun &&
        settingsRepository.adFreeEndDate.isRewardExpired() &&
        settingsRepository.sessionCount > configManager.appConfig.adConfig.bannerAdSessionCount

    // region Event
    override fun onKeyPress(key: String) {
        Logger.d { "CalculatorViewModel onKeyPress $key" }
        when (key) {
            KEY_AC -> _state.update(input = "")
            KEY_DEL ->
                state.value.input
                    .whetherNot { isEmpty() }
                    ?.apply {
                        _state.update(input = substring(0, length - 1))
                    }
            else -> _state.update(input = if (key.isEmpty()) "" else state.value.input + key)
        }
    }

    override fun onItemClick(currency: Currency) {
        Logger.d { "CalculatorViewModel onItemClick ${currency.name}" }
        var finalResult = currency.rate
            .getFormatted()
            .toStandardDigits()
            .toSupportedCharacters()

        while (finalResult.length >= MAXIMUM_OUTPUT || finalResult.length >= MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _state.update(
            base = currency.name,
            input = finalResult
        )
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        Logger.d { "CalculatorViewModel onItemLongClick ${currency.name}" }
        clientScope.launch {
            _effect.emit(
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

    override fun onBarClick() = clientScope.launchIgnored {
        Logger.d { "CalculatorViewModel onBarClick" }
        _effect.emit(CalculatorEffect.OpenBar)
    }

    override fun onSettingsClicked() = clientScope.launchIgnored {
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

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.client.model.AppTheme
import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.DataState
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.viewmodel.adremove.AdRemoveState
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarState
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorState
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesState
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow

fun MutableStateFlow<BarState>.update(
    loading: Boolean = value.loading,
    enoughCurrency: Boolean = value.enoughCurrency,
    currencyList: List<Currency> = value.currencyList
) {
    value = value.copy(
        loading = loading,
        enoughCurrency = enoughCurrency,
        currencyList = currencyList
    )
}

@Suppress("LongParameterList")
fun MutableStateFlow<CalculatorState>.update(
    input: String = value.input,
    base: String = value.base,
    currencyList: List<Currency> = value.currencyList,
    output: String = value.output,
    symbol: String = value.symbol,
    loading: Boolean = value.loading,
    dataState: DataState = value.dataState
) {
    value = value.copy(
        input = input,
        base = base,
        currencyList = currencyList,
        output = output,
        symbol = symbol,
        loading = loading,
        dataState = dataState
    )
}

fun MutableStateFlow<CurrenciesState>.update(
    currencyList: List<Currency> = value.currencyList,
    loading: Boolean = value.loading,
    selectionVisibility: Boolean = value.selectionVisibility
) {
    value = value.copy(
        currencyList = currencyList,
        loading = loading,
        selectionVisibility = selectionVisibility
    )
}

fun MutableStateFlow<SettingsState>.update(
    activeCurrencyCount: Int = value.activeCurrencyCount,
    appThemeType: AppTheme = value.appThemeType,
    addFreeEndDate: String = value.addFreeEndDate,
    loading: Boolean = value.loading
) {
    value = value.copy(
        activeCurrencyCount = activeCurrencyCount,
        appThemeType = appThemeType,
        addFreeEndDate = addFreeEndDate,
        loading = loading
    )
}

fun MutableStateFlow<AdRemoveState>.update(
    adRemoveTypes: List<RemoveAdType> = value.adRemoveTypes,
    loading: Boolean = value.loading
) {
    value = value.copy(
        adRemoveTypes = adRemoveTypes,
        loading = loading
    )
}

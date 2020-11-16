/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.ccc.android.ui.main.MainData
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.preferences.PreferencesRepository

// State
@Suppress("ConstructorParameterNaming")
data class CurrenciesState(
    private val _state: MutableCurrenciesState
) {
    val currencyList: LiveData<MutableList<Currency>> = _state._currencyList
    val loading: LiveData<Boolean> = _state._loading
    val selectionVisibility: LiveData<Boolean> = _state._selectionVisibility
}

@Suppress("ConstructorParameterNaming")
data class MutableCurrenciesState(
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(false),
    val _selectionVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
)

// Event
interface CurrenciesEvent {
    fun updateAllCurrenciesState(state: Boolean)
    fun onItemClick(currency: Currency)
    fun onDoneClick()
    fun onItemLongClick(): Boolean
    fun onCloseClick()
}

// Effect
sealed class CurrenciesEffect
object FewCurrencyEffect : CurrenciesEffect()
object CalculatorEffect : CurrenciesEffect()
object BackEffect : CurrenciesEffect()
data class ChangeBaseNavResultEffect(val newBase: String) : CurrenciesEffect()

// Data
data class CurrenciesData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository) {
    companion object {
        internal const val SPAN_PORTRAIT = 1
        internal const val SPAN_LANDSCAPE = 3
    }

    var unFilteredList: MutableList<Currency>? = mutableListOf()
    lateinit var query: String
}

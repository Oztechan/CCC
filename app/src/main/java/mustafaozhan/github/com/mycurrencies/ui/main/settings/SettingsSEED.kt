/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.ui.main.MainData

data class SettingsState(
    private val backing: SettingsStateBacking
) : BaseState() {
    val searchQuery: MutableLiveData<String> = backing._searchQuery
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
    val loading: LiveData<Boolean> = backing._loading
}

@Suppress("ConstructorParameterNaming")
data class SettingsStateBacking(
    val _searchQuery: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData<MutableList<Currency>>(),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
)

interface SettingsEvent : BaseEvent {
    fun onSelectAllClick()
    fun onDeselectAllClick()
    fun onItemClick(currency: Currency)
    fun onDoneClick()
}

sealed class SettingsEffect : BaseEffect()
object FewCurrencyEffect : SettingsEffect()
object CalculatorEffect : SettingsEffect()

data class SettingsData(
    var unFilteredList: MutableList<Currency> = mutableListOf()
) : MainData()

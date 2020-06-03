/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import com.github.mustafaozhan.basemob.model.BaseStateBacking
import mustafaozhan.github.com.mycurrencies.model.Currency

// State
data class BarState(
    private val backing: BarStateBacking
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
    val loading: LiveData<Boolean> = backing._loading
    val enoughCurrency: LiveData<Boolean> = backing._enoughCurrency
}

@Suppress("ConstructorParameterNaming")
data class BarStateBacking(
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData<MutableList<Currency>>(),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(true),
    val _enoughCurrency: MutableLiveData<Boolean> = MutableLiveData(false)
) : BaseStateBacking()

// Event
interface BarEvent : BaseEvent {
    fun onItemClick(currency: Currency)
    fun onSelectClick()
}

sealed class BarEffect : BaseEffect()
data class ChangeBaseEffect(val newBase: String) : BarEffect()
object OpenSettingsEffect : BarEffect()

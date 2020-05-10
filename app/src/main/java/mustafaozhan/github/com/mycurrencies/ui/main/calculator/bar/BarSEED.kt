/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

// State
data class BarState(
    private val backing: BarStateBacking
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
}

@Suppress("ConstructorParameterNaming")
data class BarStateBacking(
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData<MutableList<Currency>>()
)

// Event
interface BarEvent : BaseEvent {
    fun onItemClick(currency: Currency)
}

sealed class BarEffect : BaseEffect()
object BaseCurrencySelected : BarEffect()

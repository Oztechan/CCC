/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar

import com.github.mustafaozhan.basemob.model.BaseData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

// State
data class BarState(
    private val backing: BarStateBacking
) : BaseState()

@Suppress("ConstructorParameterNaming")
class BarStateBacking

// Event
interface BarEvent : BaseEvent {
    fun onItemClick(currency: Currency)
}

// Effect
sealed class BarEffect : BaseEffect()

// Data
class BarData : BaseData()

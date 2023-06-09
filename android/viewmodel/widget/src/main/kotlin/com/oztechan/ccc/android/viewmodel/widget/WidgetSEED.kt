package com.oztechan.ccc.android.viewmodel.widget

import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.common.core.model.Currency

// State
data class WidgetState(
    var currencyList: List<Currency> = listOf(),
    var lastUpdate: String = "",
    var currentBase: String,
    var isPremium: Boolean
) : BaseState()

// Event
interface WidgetEvent : BaseEvent {
    suspend fun refreshWidgetData()
    fun onPreviousClick()
}

// Data
class WidgetData : BaseData() {
    companion object {
        internal const val MAXIMUM_NUMBER_OF_CURRENCY = 7
    }
}

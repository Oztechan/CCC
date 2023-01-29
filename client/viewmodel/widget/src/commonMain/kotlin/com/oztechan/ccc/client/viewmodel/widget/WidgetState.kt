package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.common.core.model.Currency

data class WidgetState(
    var currencyList: List<Currency> = listOf(),
    var lastUpdate: String = "",
    var currentBase: String,
    var isPremium: Boolean
)

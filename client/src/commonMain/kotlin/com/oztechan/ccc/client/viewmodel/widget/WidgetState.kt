package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.model.Currency

data class WidgetState(
    var currencyList: List<Currency> = listOf(),
    var lastUpdate: String = "",
    var currentBase: String
)

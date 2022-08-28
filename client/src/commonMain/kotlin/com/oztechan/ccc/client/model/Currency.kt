/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.model

data class Currency(
    val name: String,
    val longName: String,
    val symbol: String,
    var rate: String,
    val isActive: Boolean = false
) {
    fun getVariablesOneLine() = "$name $longName $symbol"
}

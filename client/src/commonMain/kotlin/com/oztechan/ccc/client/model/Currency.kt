/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.model

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    var rate: String,
    val isActive: Boolean = false
) {
    fun getVariablesOneLine() = "$code $name $symbol"
}

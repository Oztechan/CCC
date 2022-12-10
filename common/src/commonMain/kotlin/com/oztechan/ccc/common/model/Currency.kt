/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.model

data class Currency(
    val code: String,
    val name: String,
    val symbol: String,
    var rate: Double = 0.0,
    val isActive: Boolean = false
)

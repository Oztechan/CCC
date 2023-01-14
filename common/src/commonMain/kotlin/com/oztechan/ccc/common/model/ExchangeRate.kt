/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.model

data class ExchangeRate(
    var base: String,
    var date: String? = null,
    var conversion: Conversion
)

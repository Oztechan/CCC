/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.model

data class CurrencyResponse(
    var base: String,
    var date: String? = null,
    var rates: Rates
)

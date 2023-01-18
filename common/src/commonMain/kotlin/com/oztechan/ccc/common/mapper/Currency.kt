/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.common.database.sql.Currency as CurrencyDBModel

internal fun CurrencyDBModel.toModel() = Currency(
    code = code,
    name = name,
    symbol = symbol,
    rate = rate,
    isActive = isActive == 1.toLong()
)

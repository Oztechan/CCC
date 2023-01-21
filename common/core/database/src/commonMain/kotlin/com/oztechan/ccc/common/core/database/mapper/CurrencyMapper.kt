/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.core.database.mapper

import com.oztechan.ccc.common.core.database.sql.Currency as CurrencyDBModel
import com.oztechan.ccc.common.core.model.Currency as CurrencyModel

fun CurrencyDBModel.toCurrencyModel() = CurrencyModel(
    code = code,
    name = name,
    symbol = symbol,
    rate = rate,
    isActive = isActive.toBoolean()
)

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.database.sql.Currency as CurrencyDBModel
import com.oztechan.ccc.common.model.Currency as CurrencyModel

internal fun CurrencyDBModel.toCurrencyModel() = CurrencyModel(
    code = code,
    name = name,
    symbol = symbol,
    rate = rate,
    isActive = isActive.toBoolean()
)

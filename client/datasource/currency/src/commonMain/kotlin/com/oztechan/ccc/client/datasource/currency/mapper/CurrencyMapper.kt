/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.datasource.currency.mapper

import com.oztechan.ccc.common.core.database.mapper.toBoolean
import com.oztechan.ccc.common.core.database.sql.Currency as CurrencyDBModel
import com.oztechan.ccc.common.core.model.Currency as CurrencyModel

internal fun CurrencyDBModel.toCurrencyModel() = CurrencyModel(
    code = code,
    name = name,
    symbol = symbol,
    rate = rate.toString(),
    isActive = isActive.toBoolean()
)

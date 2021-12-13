/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.mapper

import com.github.mustafaozhan.ccc.common.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.github.mustafaozhan.ccc.common.db.sql.Currency as CurrencyEntity

internal fun CurrencyEntity.toModel() = Currency(
    name = name,
    longName = longName,
    symbol = symbol,
    rate = rate,
    isActive = isActive == 1.toLong()
)

internal fun List<CurrencyEntity>.toModelList(): List<Currency> {
    return map { it.toModel() }
}

internal fun Flow<List<CurrencyEntity>>.mapToModel(): Flow<List<Currency>> {
    return this.map { it.toModelList() }
}

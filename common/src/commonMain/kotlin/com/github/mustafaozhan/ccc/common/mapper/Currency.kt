/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.mapper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.github.mustafaozhan.ccc.common.db.sql.Currency as CurrencyEntity
import com.github.mustafaozhan.ccc.common.model.Currency as CurrencyModel

fun CurrencyEntity.toModel() = CurrencyModel(
    name, longName, symbol, rate, isActive == 1.toLong()
)

fun List<CurrencyEntity>.toModelList(): List<CurrencyModel> {
    val temp = mutableListOf<CurrencyModel>()
    forEach {
        temp.add(
            CurrencyModel(
                it.name, it.longName, it.symbol, it.rate, it.isActive == 1.toLong()
            )
        )
    }
    return temp.toList()
}

fun Flow<List<CurrencyEntity>>.mapToModel(): Flow<List<CurrencyModel>> {
    return this.map {
        mutableListOf<CurrencyModel>().apply {
            it.forEach {
                add(
                    CurrencyModel(
                        it.name,
                        it.longName,
                        it.symbol,
                        it.rate,
                        it.isActive == 1.toLong()
                    )
                )
            }
        }.toList()
    }
}

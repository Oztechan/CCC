/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Currency(
    val name: String,
    val longName: String,
    val symbol: String,
    var rate: Double = 0.0,
    val isActive: Boolean = false
) {
    fun getVariablesOneLine() = "$name $longName $symbol"
}

fun com.github.mustafaozhan.ccc.common.Currency.toModel() = Currency(
    name, longName, symbol, rate, isActive == 1.toLong()
)

fun List<com.github.mustafaozhan.ccc.common.Currency>.toModelList(): List<Currency> {
    val temp = mutableListOf<Currency>()
    forEach {
        temp.add(
            Currency(
                it.name, it.longName, it.symbol, it.rate, it.isActive == 1.toLong()
            )
        )
    }
    return temp.toList()
}

fun Flow<List<com.github.mustafaozhan.ccc.common.Currency>>.mapToModel(): Flow<List<Currency>> {
    return this.map {
        mutableListOf<Currency>().apply {
            it.forEach {
                add(Currency(it.name, it.longName, it.symbol, it.rate, it.isActive == 1.toLong()))
            }
        }.toList()
    }
}

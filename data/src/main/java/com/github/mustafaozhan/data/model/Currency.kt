/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "longName") var longName: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "rate") var rate: Double = 0.0,
    @ColumnInfo(name = "isActive") var isActive: Boolean = false
) {
    fun getVariablesOneLine() = "$name $longName $symbol"
}

package com.oztechan.ccc.common.datasource.conversion

import com.oztechan.ccc.common.core.model.Conversion

interface ConversionDataSource {
    suspend fun insertConversion(conversion: Conversion)

    suspend fun getConversionByBase(baseName: String): Conversion?
}

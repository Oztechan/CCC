package com.oztechan.ccc.backend.service.free

import com.oztechan.ccc.common.core.model.Conversion

interface FreeApiService {
    suspend fun getConversion(base: String): Conversion
}

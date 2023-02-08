package com.oztechan.ccc.client.service.backend

import com.oztechan.ccc.common.core.model.Conversion

interface BackendApiService {
    suspend fun getConversion(base: String): Conversion
}

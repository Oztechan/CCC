package com.oztechan.ccc.common.core.network.api.free

import com.oztechan.ccc.common.core.network.BuildKonfig
import com.oztechan.ccc.common.core.network.model.ExchangeRate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.http.takeFrom

internal class FreeApiImpl(private val client: HttpClient) : FreeApi {
    override suspend fun getExchangeRate(base: String): ExchangeRate = client.get {
        url {
            takeFrom(BuildKonfig.BASE_URL_API)
            path(PATH_LATEST)
            parameter(QUERY_BASE, base)
        }
    }.body()

    companion object {
        private const val QUERY_BASE = "base"
        private const val PATH_LATEST = "latest"
    }
}

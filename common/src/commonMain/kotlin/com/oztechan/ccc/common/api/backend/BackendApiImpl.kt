package com.oztechan.ccc.common.api.backend

import com.oztechan.ccc.common.BuildKonfig
import com.oztechan.ccc.common.api.model.CurrencyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.http.takeFrom

internal class BackendApiImpl(private val client: HttpClient) : BackendApi {

    override suspend fun getConversion(base: String): CurrencyResponse = client.get {
        url {
            takeFrom(BuildKonfig.BASE_URL_BACKEND)
            path(PATH_CURRENCY, PATH_BY_BASE)
            parameter(QUERY_BASE, base)
        }
    }.body()

    companion object {
        private const val QUERY_BASE = "base"

        private const val PATH_CURRENCY = "currency"
        private const val PATH_BY_BASE = "byBase/"
    }
}

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.api.service

import com.oztechan.ccc.common.BuildKonfig
import com.oztechan.ccc.common.api.model.CurrencyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.http.takeFrom

internal class ApiServiceImpl(private val client: HttpClient) : ApiService {

    override suspend fun getRatesByBackend(base: String): CurrencyResponse = client.get {
        url {
            takeFrom(BuildKonfig.BASE_URL_BACKEND)
            path(PATH_CURRENCY, PATH_BY_BASE)
            parameter(QUERY_BASE, base)
        }
    }.body()

    override suspend fun getRatesByPremiumAPI(base: String): CurrencyResponse = client.get {
        url {
            takeFrom(BuildKonfig.BASE_URL_API_PREMIUM)
            path(PATH_PREMIUM_VERSION, BuildKonfig.API_KEY_PREMIUM, PATH_LATEST, base)
        }
    }.body()

    companion object {
        private const val QUERY_BASE = "base"

        private const val PATH_CURRENCY = "currency"
        private const val PATH_BY_BASE = "byBase/"
        private const val PATH_LATEST = "latest"
        private const val PATH_PREMIUM_VERSION = "v6"
    }
}

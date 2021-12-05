/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.BuildKonfig
import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.takeFrom

internal class ApiService(private val client: HttpClient) {

    suspend fun getRatesByBackend(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_BACKEND)
            path(PATH_CURRENCY_BY_BASE_BACKEND)
            parameter(QUERY_BASE, base)
        }
    }

    suspend fun getRatesByAPI(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_API)
            path(PATH_CURRENCY_BY_BASE_API)
            parameter(QUERY_BASE, base)
        }
    }

    suspend fun getRatesByPremiumAPI(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_API_PREMIUM)
            path(
                PATH_PREMIUM,
                BuildKonfig.API_KEY_PREMIUM,
                PATH_CURRENCY_BY_BASE_API,
                base
            )
        }
    }

    companion object {
        private const val QUERY_BASE = "base"

        private const val PATH_CURRENCY_BY_BASE_BACKEND = "currency/byBase/"
        private const val PATH_CURRENCY_BY_BASE_API = "latest"
        private const val PATH_PREMIUM = "v6"
    }
}

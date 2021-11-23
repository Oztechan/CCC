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

    suspend fun getRatesViaBackend(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_BACKEND)
            path(PATH_CURRENCY_BY_BASE_BACKEND)
            parameter(QUERY_BASE, base)
        }
    }

    suspend fun getUnPopularRates(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_API)
            path(PATH_CURRENCY_BY_BASE_API)
            parameter(QUERY_BASE, base)
        }
    }

    suspend fun getPopularRates(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_API_POPULAR)
            path(PATH_POPULAR)
            parameter(QUERY_KEY, BuildKonfig.API_KEY_POPULAR)
            parameter(QUERY_BASE, base)
        }
    }

    companion object {
        private const val QUERY_BASE = "base"
        private const val QUERY_KEY = "key"

        private const val PATH_CURRENCY_BY_BASE_BACKEND = "currency/byBase/"
        private const val PATH_CURRENCY_BY_BASE_API = "latest/"
        private const val PATH_POPULAR = "/api/v1/rates"
    }
}

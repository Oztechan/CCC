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

internal class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getRatesByBackend(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_BACKEND)
            path(PATH_CURRENCY, PATH_BY_BASE)
            parameter(QUERY_BASE, base)
        }
    }

    override suspend fun getRatesByAPI(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_API)
            path(PATH_LATEST)
            parameter(QUERY_BASE, base)
        }
    }

    override suspend fun getRatesByPremiumAPI(base: String) = client.get<CurrencyResponseEntity> {
        url {
            takeFrom(BuildKonfig.BASE_URL_API_PREMIUM)
            path(PATH_PREMIUM_VERSION, BuildKonfig.API_KEY_PREMIUM, PATH_LATEST, base)
        }
    }

    companion object {
        private const val QUERY_BASE = "base"

        private const val PATH_CURRENCY = "currency"
        private const val PATH_BY_BASE = "byBase/"
        private const val PATH_LATEST = "latest"
        private const val PATH_PREMIUM_VERSION = "v6"
    }
}

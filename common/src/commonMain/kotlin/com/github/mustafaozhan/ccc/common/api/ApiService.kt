/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.BuildKonfig
import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.isDebug
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.takeFrom

internal class ApiService(private val client: HttpClient) {

    suspend fun getRatesViaBackend(base: String) = client.get<CurrencyResponseEntity> {
        url {
            if (isDebug()) {
                takeFrom(BuildKonfig.BASE_URL_DEV)
            } else {
                takeFrom(BuildKonfig.BASE_URL_BACKEND)
                path(PATH_CURRENCY_BY_BASE_BACKEND)
                parameter(QUERY_KEY_BASE, base)
            }
        }
    }

    suspend fun getRatesViaApi(base: String) = client.get<CurrencyResponseEntity> {
        url {
            if (isDebug()) {
                takeFrom(BuildKonfig.BASE_URL_DEV)
            } else {
                takeFrom(BuildKonfig.BASE_URL_API)
                path(PATH_CURRENCY_BY_BASE_API)
                parameter(QUERY_KEY_BASE, base)
            }
        }
    }

    companion object {
        private const val QUERY_KEY_BASE = "base"
        private const val PATH_CURRENCY_BY_BASE_BACKEND = "currency/byBase/"
        private const val PATH_CURRENCY_BY_BASE_API = "latest/"
    }
}

/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.data.api

import com.github.mustafaozhan.ccc.common.data.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.secret.BASE_URL_API
import com.github.mustafaozhan.ccc.common.secret.BASE_URL_BACKEND
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json

class ApiFactory : ApiService {

    companion object {
        private const val TIME_OUT: Long = 3000
        private const val QUERY_KEY_BASE = "base"
        private const val PATH_CURRENCY_BY_BASE_BACKEND = "currency/byBase/"
        private const val PATH_CURRENCY_BY_BASE_API = "latest/"
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private val client by lazy {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            install(HttpTimeout) {
                connectTimeoutMillis = TIME_OUT
                socketTimeoutMillis = TIME_OUT
                requestTimeoutMillis = TIME_OUT
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        kermit.v("Network") { message }
                    }
                }
                level = LogLevel.INFO
            }
        }
    }

    override suspend fun getRatesByBaseViaBackend(base: String): CurrencyResponseEntity =
        client.get {
            url {
                takeFrom(BASE_URL_BACKEND)
                path(PATH_CURRENCY_BY_BASE_BACKEND)
                parameter(QUERY_KEY_BASE, base)
            }
        }

    override suspend fun getRatesByBaseViaApi(base: String): CurrencyResponseEntity = client.get {
        url {
            takeFrom(BASE_URL_API)
            path(PATH_CURRENCY_BY_BASE_API)
            parameter(QUERY_KEY_BASE, base)
        }
    }
}

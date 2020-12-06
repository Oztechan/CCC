/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
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
        private const val PATH_CURRENCY_BY_BASE = "currency/byBase/"
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

    override suspend fun getRatesByBase(base: String): CurrencyResponse = client.get {
        url {
            takeFrom(BASE_URL_BACKEND)
            path(PATH_CURRENCY_BY_BASE)
            parameter(QUERY_KEY_BASE, base)
        }
    }
}

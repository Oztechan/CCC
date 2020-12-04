/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.api

import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.secret.BASE_URL
import com.github.mustafaozhan.temp.CurrencyResponseV2
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class ApiFactory : ApiService {

    private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

    private val client by lazy {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(nonStrictJson)
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

    override suspend fun getRatesByBase(base: String) =
        client.get<CurrencyResponseV2>("${BASE_URL}currency/byBase/?base=$base")
}

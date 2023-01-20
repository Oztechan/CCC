package com.oztechan.ccc.common.di

import com.oztechan.ccc.common.api.backend.BackendApi
import com.oztechan.ccc.common.api.backend.BackendApiImpl
import com.oztechan.ccc.common.api.free.FreeApi
import com.oztechan.ccc.common.api.free.FreeApiImpl
import com.oztechan.ccc.common.api.premium.PremiumApi
import com.oztechan.ccc.common.api.premium.PremiumApiImpl
import com.oztechan.ccc.common.util.KtorLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val TIME_OUT: Long = 3333

val apiModule = module {
    singleOf(::provideHttpClient)

    singleOf(::FreeApiImpl) { bind<FreeApi>() }
    singleOf(::BackendApiImpl) { bind<BackendApi>() }
    singleOf(::PremiumApiImpl) { bind<PremiumApi>() }
}

private fun provideHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(
            json = Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
            contentType = ContentType.Any
        )
    }
    install(HttpTimeout) {
        connectTimeoutMillis = TIME_OUT
        socketTimeoutMillis = TIME_OUT
        requestTimeoutMillis = TIME_OUT
    }
    install(Logging) {
        level = LogLevel.INFO
        logger = KtorLogger()
    }
}

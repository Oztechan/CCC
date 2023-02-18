package com.oztechan.ccc.common.core.network.di

import com.oztechan.ccc.common.core.network.api.backend.BackendApi
import com.oztechan.ccc.common.core.network.api.backend.BackendApiImpl
import com.oztechan.ccc.common.core.network.api.free.FreeApi
import com.oztechan.ccc.common.core.network.api.free.FreeApiImpl
import com.oztechan.ccc.common.core.network.api.premium.PremiumApi
import com.oztechan.ccc.common.core.network.api.premium.PremiumApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val TIME_OUT: Long = 3333

val commonCoreNetworkModule = module {
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
        )
    }
    install(HttpTimeout) {
        connectTimeoutMillis = TIME_OUT
        socketTimeoutMillis = TIME_OUT
        requestTimeoutMillis = TIME_OUT
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}

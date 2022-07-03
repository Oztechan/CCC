package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.api.repo.ApiRepository
import com.oztechan.ccc.common.api.repo.ApiRepositoryImpl
import com.oztechan.ccc.common.api.service.ApiService
import com.oztechan.ccc.common.api.service.ApiServiceImpl
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
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val TIME_OUT: Long = 3333

val apiModule = module {
    singleOf(::provideHttpClient)
    factoryOf(::ApiServiceImpl) { bind<ApiService>() }
    factoryOf(::ApiRepositoryImpl) { bind<ApiRepository>() }
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

package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.api.ApiRepository
import com.github.mustafaozhan.ccc.common.api.ApiRepositoryImpl
import com.github.mustafaozhan.ccc.common.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val TIME_OUT: Long = 3000

val apiModule = module {
    single { provideSerializer() }
    single { provideHttpClient(get()) }
    factory { ApiService(get()) }
    single<ApiRepository> { ApiRepositoryImpl(get()) }
}

private fun provideSerializer() = KotlinxSerializer(
    Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
)

private fun provideHttpClient(kotlinxSerializer: KotlinxSerializer) = HttpClient {
    install(JsonFeature) {
        serializer = kotlinxSerializer
        accept(ContentType.Application.Json)
        accept(ContentType.Text.Plain)
    }
    install(HttpTimeout) {
        connectTimeoutMillis = TIME_OUT
        socketTimeoutMillis = TIME_OUT
        requestTimeoutMillis = TIME_OUT
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.INFO
    }
}

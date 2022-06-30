package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.api.repo.ApiRepository
import com.oztechan.ccc.common.api.repo.ApiRepositoryImpl
import com.oztechan.ccc.common.api.service.ApiService
import com.oztechan.ccc.common.api.service.ApiServiceImpl
import com.oztechan.ccc.common.di.DISPATCHER_IO
import com.oztechan.ccc.common.util.KtorLogger
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val TIME_OUT: Long = 3333

val apiModule = module {
    single { provideSerializer() }
    single { provideHttpClient(get()) }
    factory<ApiService> { ApiServiceImpl(get()) }
    single<ApiRepository> { ApiRepositoryImpl(get(), get(named(DISPATCHER_IO))) }
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
        accept(ContentType.Text.Html)
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

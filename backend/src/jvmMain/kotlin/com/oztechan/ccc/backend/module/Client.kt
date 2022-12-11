package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.repository.api.ApiRepository
import io.ktor.server.application.Application
import org.koin.java.KoinJavaComponent.inject

@Suppress("unused", "UnusedReceiverParameter")
internal fun Application.client() {
    val apiRepository: ApiRepository by inject(ApiRepository::class.java)

    Logger.i { "start sync" }
    apiRepository.startSyncApi()
}

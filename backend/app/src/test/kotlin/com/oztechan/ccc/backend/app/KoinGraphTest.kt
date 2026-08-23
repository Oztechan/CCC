package com.oztechan.ccc.backend.app

import com.oztechan.ccc.backend.controller.api.di.backendControllerAPIModule
import com.oztechan.ccc.backend.controller.sync.di.backendControllerSyncModule
import com.oztechan.ccc.backend.service.premium.di.backendServicePremiumModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataSourceConversionModule
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

internal class KoinGraphTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `backend Koin graph has all dependencies declared`() {
        module {
            includes(
                backendServicePremiumModule,
                backendControllerSyncModule,
                backendControllerAPIModule,
                commonCoreDatabaseModule,
                commonCoreNetworkModule,
                commonCoreInfrastructureModule,
                commonDataSourceConversionModule
            )
        }.verify(
            // Ktor picks the HttpClientEngine implicitly at runtime, so it has no Koin binding.
            extraTypes = listOf(HttpClientEngine::class)
        )
    }
}

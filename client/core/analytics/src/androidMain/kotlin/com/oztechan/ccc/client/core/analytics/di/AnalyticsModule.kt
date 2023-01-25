package com.oztechan.ccc.client.core.analytics.di

import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.AnalyticsManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::AnalyticsManagerImpl) { bind<AnalyticsManager>() }
}

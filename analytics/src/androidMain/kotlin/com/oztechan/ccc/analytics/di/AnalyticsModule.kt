package com.oztechan.ccc.analytics.di

import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.AnalyticsManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::AnalyticsManagerImpl) { bind<AnalyticsManager>() }
}

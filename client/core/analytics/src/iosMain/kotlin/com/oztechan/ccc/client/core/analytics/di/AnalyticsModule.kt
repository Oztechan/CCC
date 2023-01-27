package com.oztechan.ccc.client.core.analytics.di

import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import org.koin.dsl.module

fun getAnalyticsModule(analyticsManager: AnalyticsManager) = module {
    single { analyticsManager }
}

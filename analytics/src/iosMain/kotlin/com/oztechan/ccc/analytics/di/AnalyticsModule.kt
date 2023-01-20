package com.oztechan.ccc.analytics.di

import com.oztechan.ccc.analytics.AnalyticsManager
import org.koin.dsl.module

fun getAnalyticsModule(analyticsManager: AnalyticsManager) = module {
    single { analyticsManager }
}

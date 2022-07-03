package com.oztechan.ccc.android.di

import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.ad.AdManagerImpl
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.AnalyticsManagerImpl
import com.oztechan.ccc.billing.BillingManager
import com.oztechan.ccc.billing.BillingManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

var platformModule = module {
    singleOf(::BillingManagerImpl) { bind<BillingManager>() }
    singleOf(::AdManagerImpl) { bind<AdManager>() }
    singleOf(::AnalyticsManagerImpl) { bind<AnalyticsManager>() }
}
